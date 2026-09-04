package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.model.Pedido;
import com.mauhernandez.ecommerceapi.repository.PedidoRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.backend-url}")
    private String backendUrl;

    private final PedidoRepository pedidoRepository;

    public MercadoPagoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public String crearPreferencia(Pedido pedido) {
        try {
            List<PreferenceItemRequest> items = pedido.getItems().stream()
                    .map(item -> PreferenceItemRequest.builder()
                            .title(item.getProducto().getNombre())
                            .quantity(item.getCantidad())
                            .unitPrice(item.getPrecioUnitario())
                            .currencyId("UYU")
                            .build())
                    .toList();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(frontendUrl + "/pedidos?pago=exito")
                    .failure(frontendUrl + "/pedidos?pago=fallo")
                    .pending(frontendUrl + "/pedidos?pago=pendiente")
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    //.autoReturn("approved") //TODO: Volver a activar cuando se haga el deploy
                    .externalReference(pedido.getId().toString())
                    .notificationUrl(backendUrl + "/api/pagos/webhook")
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);
            return preference.getInitPoint();

        } catch (MPApiException e) {
            System.out.println("Respuesta de MercadoPago: " + e.getApiResponse().getContent());
            throw new RuntimeException("Error al crear la preferencia de pago: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException("Error al crear la preferencia de pago: " + e.getMessage());
        }
    }

    public void procesarNotificacion(Map<String, String> params, Map<String, Object> body) {
        String tipo = params.getOrDefault("type", params.get("topic"));
        String paymentId = params.get("data.id");

        if (paymentId == null && body != null) {
            Object data = body.get("data");
            if (data instanceof Map<?, ?> dataMap) {
                paymentId = String.valueOf(dataMap.get("id"));
            }
        }

        if (!"payment".equals(tipo) || paymentId == null) {
            return; // Ignoramos otros tipos de notificación (ej: merchant_order)
        }

        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            Long pedidoId = Long.parseLong(payment.getExternalReference());
            Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
            if (pedido == null) return;

            String status = payment.getStatus(); // approved, pending, rejected, etc.

            if ("approved".equals(status)) {
                pedido.setEstado(Pedido.Estado.PAGADO);
                pedidoRepository.save(pedido);
            } else if ("rejected".equals(status)) {
                pedido.setEstado(Pedido.Estado.CANCELADO);
                pedidoRepository.save(pedido);
            }
            // "pending" y otros estados: no tocamos el pedido, sigue como estaba

        } catch (MPApiException | MPException e) {
            System.out.println("Error procesando webhook: " + e.getMessage());
        }
    }
}
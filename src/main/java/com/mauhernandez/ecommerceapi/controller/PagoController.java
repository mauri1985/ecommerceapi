package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Pedido;
import com.mauhernandez.ecommerceapi.repository.PedidoRepository;
import com.mauhernandez.ecommerceapi.service.MercadoPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final MercadoPagoService mercadoPagoService;
    private final PedidoRepository pedidoRepository;

    public PagoController(MercadoPagoService mercadoPagoService, PedidoRepository pedidoRepository) {
        this.mercadoPagoService = mercadoPagoService;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping("/preferencia/{pedidoId}")
    public Map<String, String> crearPreferencia(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con id: " + pedidoId));

        String urlPago = mercadoPagoService.crearPreferencia(pedido);
        return Map.of("urlPago", urlPago);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> recibirWebhook(@RequestParam Map<String, String> params, @RequestBody(required = false) Map<String, Object> body) {
        mercadoPagoService.procesarNotificacion(params, body);
        return ResponseEntity.ok().build();
    }

}
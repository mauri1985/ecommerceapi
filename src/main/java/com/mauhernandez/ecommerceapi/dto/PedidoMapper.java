package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.Pedido;
import com.mauhernandez.ecommerceapi.model.PedidoItem;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        List<PedidoItemResponse> items = pedido.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getEstado().name(),
                pedido.getTotal(),
                items
        );
    }

    private PedidoItemResponse toItemResponse(PedidoItem item) {
        var subtotal = item.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(item.getCantidad()));

        return new PedidoItemResponse(
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                subtotal
        );
    }
}
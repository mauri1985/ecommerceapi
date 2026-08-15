package com.mauhernandez.ecommerceapi.dto;

import java.math.BigDecimal;

public record PedidoItemResponse(
        Long productoId,
        String productoNombre,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}
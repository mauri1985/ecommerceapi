package com.mauhernandez.ecommerceapi.dto;

import java.math.BigDecimal;

public record CarritoItemResponse(
        Long id,
        Long productoId,
        String productoNombre,
        BigDecimal precioUnitario,
        Integer cantidad,
        BigDecimal subtotal
) {}
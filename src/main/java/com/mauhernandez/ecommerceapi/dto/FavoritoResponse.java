package com.mauhernandez.ecommerceapi.dto;

import java.math.BigDecimal;
import java.util.List;

public record FavoritoResponse(
        Long productoId,
        String productoNombre,
        BigDecimal precio,
        BigDecimal precioOferta,
        Integer porcentajeDescuento,
        List<String> imagenes,
        Boolean activo,
        Integer stock
) {}
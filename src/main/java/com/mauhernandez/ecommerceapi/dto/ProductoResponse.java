package com.mauhernandez.ecommerceapi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        BigDecimal precioOferta,
        Integer porcentajeDescuento,
        Integer stock,
        String categoriaNombre,
        Map<String, Object> atributos,
        Boolean activo,
        List<String> imagenes,
        Boolean destacado,
        List<ImagenInfo> imagenesCompletas
) {}

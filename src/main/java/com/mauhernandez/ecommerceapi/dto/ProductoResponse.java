package com.mauhernandez.ecommerceapi.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String categoriaNombre,
        Map<String, Object> atributos,
        Boolean activo
) {}

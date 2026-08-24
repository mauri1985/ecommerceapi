package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;

public record ProductoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        Map<String, Object> atributos,

        @DecimalMin(value = "0.0", inclusive = false, message = "El precio de oferta debe ser mayor a 0")
        BigDecimal precioOferta,

        Boolean destacado
) {}
package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CarritoItemRequest(

        @NotNull(message = "El id de usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "El id de producto es obligatorio")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad
) {}
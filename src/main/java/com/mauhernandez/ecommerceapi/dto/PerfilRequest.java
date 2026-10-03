package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.NotBlank;

public record PerfilRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        String apellido,
        String telefono,
        String direccionCalle,
        String ciudad
) {}

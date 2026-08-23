package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.NotBlank;

public record ImagenRequest(
        @NotBlank(message = "La URL es obligatoria")
        String url
) {}
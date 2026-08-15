package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        Long categoriaPadreId
) {}
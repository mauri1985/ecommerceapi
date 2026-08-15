package com.mauhernandez.ecommerceapi.dto;

public record CategoriaResponse(
        Long id,
        String nombre,
        Long categoriaPadreId,
        String categoriaPadreNombre
) {}
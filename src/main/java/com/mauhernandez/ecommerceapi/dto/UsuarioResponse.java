package com.mauhernandez.ecommerceapi.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String rol,
        LocalDateTime fechaCreacion
) {}
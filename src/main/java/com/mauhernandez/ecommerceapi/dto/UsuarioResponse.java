package com.mauhernandez.ecommerceapi.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String direccionCalle,
        String ciudad,
        String rol,
        LocalDateTime fechaCreacion
) {}
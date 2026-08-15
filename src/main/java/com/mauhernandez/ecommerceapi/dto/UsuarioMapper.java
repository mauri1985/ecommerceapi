package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password()); // sin encriptar todavía — pendiente hasta JWT
        usuario.setRol(Usuario.Rol.CLIENTE);
        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getFechaCreacion()
        );
    }
}
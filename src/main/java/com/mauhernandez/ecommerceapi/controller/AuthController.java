package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.config.JwtService;
import com.mauhernandez.ecommerceapi.dto.LoginRequest;
import com.mauhernandez.ecommerceapi.dto.LoginResponse;
import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(request.email())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getEmail());
        return new LoginResponse(token, usuario.getEmail(), usuario.getNombre());
    }
}
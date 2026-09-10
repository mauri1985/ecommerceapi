package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.config.JwtService;
import com.mauhernandez.ecommerceapi.dto.*;
import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.TokenAccion;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.service.EmailService;
import com.mauhernandez.ecommerceapi.service.GoogleAuthService;
import com.mauhernandez.ecommerceapi.service.TokenAccionService;
import com.mauhernandez.ecommerceapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenAccionService tokenAccionService;
    private final EmailService emailService;
    private GoogleAuthService googleAuthService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService, TokenAccionService tokenAccionService, EmailService emailService,GoogleAuthService googleAuthService ) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenAccionService = tokenAccionService;
        this.emailService = emailService;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(request.email())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getEmail());
        return new LoginResponse(usuario.getId(), token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().name());
    }

    @GetMapping("/verificar-email")
    public void verificarEmail(@RequestParam String token) {
        Usuario usuario = tokenAccionService.validarYUsar(token, TokenAccion.Tipo.VERIFICACION_EMAIL);
        usuario.setEmailVerificado(true);
        usuarioService.guardarSinRevalidar(usuario);
    }

    @PostMapping("/solicitar-reset")
    public void solicitarReset(@Valid @RequestBody SolicitarResetRequest request) {
        usuarioService.buscarPorEmail(request.email()).ifPresent(usuario -> {
            String token = tokenAccionService.generar(usuario, TokenAccion.Tipo.RESET_PASSWORD);
            String link = construirLink("/verificar-email?token=" + token);
            emailService.enviar(usuario.getEmail(), "Recuperar contraseña",
                    "Hacé click para elegir una nueva contraseña:\n\n" + link);
        });
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        Usuario usuario = tokenAccionService.validarYUsar(request.token(), TokenAccion.Tipo.RESET_PASSWORD);
        usuarioService.cambiarPassword(usuario, request.nuevaPassword());
    }

    private String construirLink(String path) {
        String base = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        return base + path;
    }

    @PostMapping("/google")
    public LoginResponse loginConGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        var payload = googleAuthService.verificar(request.credential());

        String email = payload.getEmail();
        String nombre = (String) payload.get("name");

        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseGet(() -> usuarioService.crearDesdeGoogle(email, nombre));

        String token = jwtService.generarToken(usuario.getEmail());
        return new LoginResponse(usuario.getId(), token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().name());
    }
}
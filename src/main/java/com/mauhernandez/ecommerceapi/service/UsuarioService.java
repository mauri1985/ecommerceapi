package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.ConflictoDeNegocioException;
import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.TokenAccion;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenAccionService tokenAccionService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService, TokenAccionService tokenAccionService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenAccionService = tokenAccionService;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ConflictoDeNegocioException("Ya existe un usuario registrado con ese email");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);

        String token = tokenAccionService.generar(guardado, TokenAccion.Tipo.VERIFICACION_EMAIL);
        String link = construirLink("/verificar-email?token=" + token);
        emailService.enviar(
                guardado.getEmail(),
                "Confirmá tu cuenta",
                "¡Bienvenido a MauriShop! Hacé click en este enlace para confirmar tu cuenta:\n\n" + link
        );

        return guardado;
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario promoverA(Long id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        Usuario.Rol rol = Usuario.Rol.valueOf(nuevoRol.toUpperCase());
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    public void guardarSinRevalidar(Usuario usuario) {
        usuarioRepository.save(usuario); // sin chequear email duplicado, ya existe
    }

    public void cambiarPassword(Usuario usuario, String nuevaPassword) {
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    private String construirLink(String path) {
        String base = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        return base + path;
    }

    public Usuario crearDesdeGoogle(String email, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setPassword(null);
        usuario.setRol(Usuario.Rol.CLIENTE);
        usuario.setEmailVerificado(true); // Google ya verificó el email por nosotros
        usuario.setProveedorAuth("GOOGLE");
        return usuarioRepository.save(usuario);
    }
}
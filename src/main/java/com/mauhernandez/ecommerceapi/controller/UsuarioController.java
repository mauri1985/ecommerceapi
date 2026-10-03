package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.*;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listarTodos().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuarioMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        Usuario guardado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(usuarioMapper.toResponse(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponse> promoverRol(@PathVariable Long id, @Valid @RequestBody PromoverRolRequest request) {
        Usuario actualizado = usuarioService.promoverA(id, request.rol());
        return ResponseEntity.ok(usuarioMapper.toResponse(actualizado));
    }

    @PutMapping("/{id}/perfil")
    public UsuarioResponse actualizarPerfil(@PathVariable Long id, @Valid @RequestBody PerfilRequest request) {
        Usuario actualizado = usuarioService.actualizarPerfil(id, request);
        return usuarioMapper.toResponse(actualizado);
    }

    @PutMapping("/{id}/cambiar-password")
    public void cambiarPassword(@PathVariable Long id, @Valid @RequestBody CambiarPasswordRequest request) {
        usuarioService.cambiarPasswordConActual(id, request.passwordActual(), request.passwordNueva());
    }
}
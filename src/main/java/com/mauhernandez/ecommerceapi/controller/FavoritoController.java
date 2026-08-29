package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.FavoritoMapper;
import com.mauhernandez.ecommerceapi.dto.FavoritoResponse;
import com.mauhernandez.ecommerceapi.service.FavoritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final FavoritoMapper favoritoMapper;

    public FavoritoController(FavoritoService favoritoService, FavoritoMapper favoritoMapper) {
        this.favoritoService = favoritoService;
        this.favoritoMapper = favoritoMapper;
    }

    @GetMapping("/{usuarioId}")
    public List<FavoritoResponse> listar(@PathVariable Long usuarioId) {
        return favoritoService.listarPorUsuario(usuarioId).stream()
                .map(favoritoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{usuarioId}/{productoId}")
    public Map<String, Boolean> esFavorito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        return Map.of("esFavorito", favoritoService.esFavorito(usuarioId, productoId));
    }

    @PostMapping("/{usuarioId}/{productoId}")
    public ResponseEntity<Void> agregar(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        favoritoService.agregar(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usuarioId}/{productoId}")
    public ResponseEntity<Void> quitar(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        favoritoService.quitar(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }
}
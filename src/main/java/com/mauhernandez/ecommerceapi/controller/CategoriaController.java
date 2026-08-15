package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.CategoriaMapper;
import com.mauhernandez.ecommerceapi.dto.CategoriaRequest;
import com.mauhernandez.ecommerceapi.dto.CategoriaResponse;
import com.mauhernandez.ecommerceapi.model.Categoria;
import com.mauhernandez.ecommerceapi.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listarTodas().stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(categoriaMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoriaPadre = resolverPadre(request.categoriaPadreId());

        Categoria categoria = categoriaMapper.toEntity(request, categoriaPadre);
        Categoria guardada = categoriaService.guardar(categoria);

        return ResponseEntity.ok(categoriaMapper.toResponse(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        return categoriaService.buscarPorId(id)
                .map(existente -> {
                    Categoria categoriaPadre = resolverPadre(request.categoriaPadreId());
                    categoriaMapper.actualizarEntity(existente, request, categoriaPadre);
                    Categoria actualizada = categoriaService.guardar(existente);
                    return ResponseEntity.ok(categoriaMapper.toResponse(actualizada));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Categoria resolverPadre(Long categoriaPadreId) {
        if (categoriaPadreId == null) {
            return null;
        }
        return categoriaService.buscarPorId(categoriaPadreId)
                .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada con id: " + categoriaPadreId));
    }
}
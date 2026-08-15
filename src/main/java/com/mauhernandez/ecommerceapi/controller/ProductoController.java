package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.ProductoMapper;
import com.mauhernandez.ecommerceapi.dto.ProductoRequest;
import com.mauhernandez.ecommerceapi.dto.ProductoResponse;
import com.mauhernandez.ecommerceapi.model.Categoria;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.service.CategoriaService;
import com.mauhernandez.ecommerceapi.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoController(ProductoService productoService,
                              CategoriaService categoriaService,
                              ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoService.listarActivos().stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(productoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.buscarPorId(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoriaId()));

        Producto producto = productoMapper.toEntity(request, categoria);
        Producto guardado = productoService.guardar(producto);

        return ResponseEntity.ok(productoMapper.toResponse(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.buscarPorId(id)
                .map(existente -> {
                    Categoria categoria = categoriaService.buscarPorId(request.categoriaId())
                            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoriaId()));

                    productoMapper.actualizarEntity(existente, request, categoria);
                    Producto actualizado = productoService.guardar(existente);

                    return ResponseEntity.ok(productoMapper.toResponse(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
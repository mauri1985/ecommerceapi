package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.ProductoMapper;
import com.mauhernandez.ecommerceapi.dto.ProductoRequest;
import com.mauhernandez.ecommerceapi.dto.ProductoResponse;
import com.mauhernandez.ecommerceapi.model.Categoria;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.service.CategoriaService;
import com.mauhernandez.ecommerceapi.service.ImagenService;
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
    private final ImagenService imagenService;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoController(ProductoService productoService,
                              CategoriaService categoriaService, ImagenService imagenService,
                              ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.imagenService = imagenService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(required = false) Long categoriaId) {
        List<Producto> productos = categoriaId != null
                ? productoService.listarPorCategoria(categoriaId)
                : productoService.listarActivos();

        return productos.stream()
                .map(p -> productoMapper.toResponse(p, imagenService.listarPorProducto(p.getId())))
                .toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(p -> productoMapper.toResponse(p, imagenService.listarPorProducto(p.getId())))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.buscarPorId(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoriaId()));

        Producto producto = productoMapper.toEntity(request, categoria);
        Producto guardado = productoService.guardar(producto);

        return ResponseEntity.ok(productoMapper.toResponse(guardado, List.of()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.buscarPorId(id)
                .map(existente -> {
                    Categoria categoria = categoriaService.buscarPorId(request.categoriaId())
                            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoriaId()));

                    productoMapper.actualizarEntity(existente, request, categoria);
                    Producto actualizado = productoService.guardar(existente);

                    List<com.mauhernandez.ecommerceapi.model.Imagen> imagenes = imagenService.listarPorProducto(id);
                    return ResponseEntity.ok(productoMapper.toResponse(actualizado, imagenes));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
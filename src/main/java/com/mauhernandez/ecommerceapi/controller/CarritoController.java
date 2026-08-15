package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.CarritoItemMapper;
import com.mauhernandez.ecommerceapi.dto.CarritoItemRequest;
import com.mauhernandez.ecommerceapi.dto.CarritoItemResponse;
import com.mauhernandez.ecommerceapi.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoItemMapper carritoItemMapper;

    @Autowired
    public CarritoController(CarritoService carritoService, CarritoItemMapper carritoItemMapper) {
        this.carritoService = carritoService;
        this.carritoItemMapper = carritoItemMapper;
    }

    @GetMapping("/{usuarioId}")
    public List<CarritoItemResponse> listar(@PathVariable Long usuarioId) {
        return carritoService.listarPorUsuario(usuarioId).stream()
                .map(carritoItemMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<CarritoItemResponse> agregar(@Valid @RequestBody CarritoItemRequest request) {
        var item = carritoService.agregarProducto(request.usuarioId(), request.productoId(), request.cantidad());
        return ResponseEntity.ok(carritoItemMapper.toResponse(item));
    }

    @DeleteMapping("/item/{carritoItemId}")
    public ResponseEntity<Void> quitarItem(@PathVariable Long carritoItemId) {
        carritoService.quitarItem(carritoItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> vaciar(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
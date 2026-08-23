package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.ImagenRequest;
import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Imagen;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.service.ImagenService;
import com.mauhernandez.ecommerceapi.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos/{productoId}/imagenes")
public class ImagenController {

    private final ImagenService imagenService;
    private final ProductoService productoService;

    public ImagenController(ImagenService imagenService, ProductoService productoService) {
        this.imagenService = imagenService;
        this.productoService = productoService;
    }

    @PostMapping
    public Imagen agregar(@PathVariable Long productoId, @Valid @RequestBody ImagenRequest request) {
        Producto producto = productoService.buscarPorId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));

        Imagen imagen = new Imagen();
        imagen.setProducto(producto);
        imagen.setUrl(request.url());
        imagen.setOrden(0);

        return imagenService.guardar(imagen);
    }
}
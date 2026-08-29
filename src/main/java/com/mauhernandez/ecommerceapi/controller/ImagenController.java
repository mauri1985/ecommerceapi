package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Imagen;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.service.CloudinaryService;
import com.mauhernandez.ecommerceapi.service.ImagenService;
import com.mauhernandez.ecommerceapi.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/productos/{productoId}/imagenes")
public class ImagenController {

    private final ImagenService imagenService;
    private final ProductoService productoService;
    private final CloudinaryService cloudinaryService;

    public ImagenController(ImagenService imagenService, ProductoService productoService, CloudinaryService cloudinaryService) {
        this.imagenService = imagenService;
        this.productoService = productoService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public Imagen subir(@PathVariable Long productoId, @RequestParam("archivo") MultipartFile archivo) {
        Producto producto = productoService.buscarPorId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));

        String url = cloudinaryService.subirImagen(archivo);

        Imagen imagen = new Imagen();
        imagen.setProducto(producto);
        imagen.setUrl(url);
        imagen.setOrden(0);

        return imagenService.guardar(imagen);
    }

    @DeleteMapping("/{imagenId}")
    public void eliminar(@PathVariable Long productoId, @PathVariable Long imagenId) {
        imagenService.eliminar(imagenId);
    }

    @PutMapping("/{imagenId}/portada")
    public void marcarComoPortada(@PathVariable Long productoId, @PathVariable Long imagenId) {
        imagenService.marcarComoPortada(productoId, imagenId);
    }
}
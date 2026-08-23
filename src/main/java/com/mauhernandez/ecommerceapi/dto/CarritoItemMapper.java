package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.CarritoItem;
import com.mauhernandez.ecommerceapi.model.Imagen;
import com.mauhernandez.ecommerceapi.service.ImagenService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarritoItemMapper {

    private final ImagenService imagenService;

    public CarritoItemMapper(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    public CarritoItemResponse toResponse(CarritoItem item) {
        var producto = item.getProducto();
        var subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));

        List<String> urls = imagenService.listarPorProducto(producto.getId())
                .stream()
                .map(Imagen::getUrl)
                .toList();

        return new CarritoItemResponse(
                item.getId(),
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                item.getCantidad(),
                subtotal,
                urls
        );
    }
}
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

        BigDecimal precioAUsar = (producto.getPrecioOferta() != null
                && producto.getPrecioOferta().compareTo(producto.getPrecio()) < 0)
                ? producto.getPrecioOferta()
                : producto.getPrecio();

        var subtotal = precioAUsar.multiply(BigDecimal.valueOf(item.getCantidad()));

        List<String> urls = imagenService.listarPorProducto(producto.getId())
                .stream()
                .map(Imagen::getUrl)
                .toList();

        return new CarritoItemResponse(
                item.getId(),
                producto.getId(),
                producto.getNombre(),
                precioAUsar,
                item.getCantidad(),
                subtotal,
                urls
        );
    }
}
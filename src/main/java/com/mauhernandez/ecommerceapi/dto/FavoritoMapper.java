package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.Favorito;
import com.mauhernandez.ecommerceapi.model.Imagen;
import com.mauhernandez.ecommerceapi.service.ImagenService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class FavoritoMapper {

    private final ImagenService imagenService;

    public FavoritoMapper(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    public FavoritoResponse toResponse(Favorito favorito) {
        var producto = favorito.getProducto();

        Integer porcentajeDescuento = null;
        if (producto.getPrecioOferta() != null && producto.getPrecioOferta().compareTo(producto.getPrecio()) < 0) {
            BigDecimal diferencia = producto.getPrecio().subtract(producto.getPrecioOferta());
            BigDecimal porcentaje = diferencia
                    .divide(producto.getPrecio(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            porcentajeDescuento = porcentaje.setScale(0, RoundingMode.HALF_UP).intValue();
        }

        List<String> urls = imagenService.listarPorProducto(producto.getId())
                .stream()
                .map(Imagen::getUrl)
                .toList();

        return new FavoritoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getPrecioOferta(),
                porcentajeDescuento,
                urls,
                producto.getActivo(),
                producto.getStock()
        );
    }
}
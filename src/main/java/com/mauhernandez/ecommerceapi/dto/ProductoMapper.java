package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.Categoria;
import com.mauhernandez.ecommerceapi.model.Imagen;
import com.mauhernandez.ecommerceapi.model.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setPrecioOferta(request.precioOferta());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
        producto.setAtributos(request.atributos());
        producto.setActivo(true);
        producto.setDestacado(request.destacado() != null ? request.destacado() : false);
        return producto;
    }

    public void actualizarEntity(Producto producto, ProductoRequest request, Categoria categoria) {
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setPrecio(request.precio());
        producto.setPrecioOferta(request.precioOferta());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
        producto.setDestacado(request.destacado() != null ? request.destacado() : false);
        producto.setAtributos(request.atributos());
    }

    public ProductoResponse toResponse(Producto producto, List<Imagen> imagenes) {
        List<String> urls = imagenes.stream().map(Imagen::getUrl).toList();
        List<ImagenInfo> imagenesInfo = imagenes.stream()
                .map(img -> new ImagenInfo(img.getId(), img.getUrl()))
                .toList();

        Integer porcentajeDescuento = null;
        if (producto.getPrecioOferta() != null && producto.getPrecioOferta().compareTo(producto.getPrecio()) < 0) {
            BigDecimal diferencia = producto.getPrecio().subtract(producto.getPrecioOferta());
            BigDecimal porcentaje = diferencia
                    .divide(producto.getPrecio(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            porcentajeDescuento = porcentaje.setScale(0, RoundingMode.HALF_UP).intValue();
        }

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getPrecioOferta(),
                porcentajeDescuento,
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getAtributos(),
                producto.getActivo(),
                urls,
                producto.getDestacado(),
                imagenesInfo
        );
    }
}

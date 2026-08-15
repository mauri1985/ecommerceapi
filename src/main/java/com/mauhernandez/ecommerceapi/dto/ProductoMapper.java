package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.Categoria;
import com.mauhernandez.ecommerceapi.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
        producto.setAtributos(request.atributos());
        producto.setActivo(true);
        return producto;
    }

    public void actualizarEntity(Producto producto, ProductoRequest request, Categoria categoria) {
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
        producto.setAtributos(request.atributos());
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getAtributos(),
                producto.getActivo()
        );
    }
}

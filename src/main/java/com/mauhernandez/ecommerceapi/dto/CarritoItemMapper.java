package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.CarritoItem;
import org.springframework.stereotype.Component;

@Component
public class CarritoItemMapper {

    public CarritoItemResponse toResponse(CarritoItem item) {
        var producto = item.getProducto();
        var subtotal = producto.getPrecio().multiply(java.math.BigDecimal.valueOf(item.getCantidad()));

        return new CarritoItemResponse(
                item.getId(),
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                item.getCantidad(),
                subtotal
        );
    }
}
package com.mauhernandez.ecommerceapi.dto;

import com.mauhernandez.ecommerceapi.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequest request, Categoria categoriaPadre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setCategoriaPadre(categoriaPadre);
        return categoria;
    }

    public void actualizarEntity(Categoria categoria, CategoriaRequest request, Categoria categoriaPadre) {
        categoria.setNombre(request.nombre());
        categoria.setCategoriaPadre(categoriaPadre);
    }

    public CategoriaResponse toResponse(Categoria categoria) {
        Categoria padre = categoria.getCategoriaPadre();
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                padre != null ? padre.getId() : null,
                padre != null ? padre.getNombre() : null
        );
    }
}

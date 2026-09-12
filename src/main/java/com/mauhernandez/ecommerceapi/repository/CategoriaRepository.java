package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByCategoriaPadreId(Long categoriaPadreId);
}

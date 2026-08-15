package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

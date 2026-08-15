package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByProductoIdOrderByOrdenAsc(Long productoId);
}

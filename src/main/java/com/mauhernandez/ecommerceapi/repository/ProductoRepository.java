package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    Page<Producto> findByActivoTrue(Pageable pageable);
    Page<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);

    Page<Producto> findByActivoTrueAndNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Producto> findByCategoriaIdAndActivoTrueAndNombreContainingIgnoreCase(Long categoriaId, String nombre, Pageable pageable);
}
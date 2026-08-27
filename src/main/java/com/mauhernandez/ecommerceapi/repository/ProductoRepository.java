package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    Page<Producto> findByActivoTrue(Pageable pageable);
    Page<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);

    Page<Producto> findByActivoTrueAndNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Producto> findByCategoriaIdAndActivoTrueAndNombreContainingIgnoreCase(Long categoriaId, String nombre, Pageable pageable);

    List<Producto> findByActivoTrueAndDestacadoTrue();

    @Query("SELECT p FROM Producto p WHERE p.activo = true " +
            "AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Producto> buscarPorNombreOCategoria(@Param("q") String q, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.categoria.id = :categoriaId " +
            "AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Producto> buscarPorNombreOCategoriaYCategoriaId(@Param("categoriaId") Long categoriaId, @Param("q") String q, Pageable pageable);
}
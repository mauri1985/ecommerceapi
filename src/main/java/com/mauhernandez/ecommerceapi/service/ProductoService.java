package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Page<Producto> listarActivosPaginado(Pageable pageable) {
        return productoRepository.findByActivoTrue(pageable);
    }

    public Page<Producto> listarPorCategoriaPaginado(Long categoriaId, Pageable pageable) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageable);
    }

    public Page<Producto> buscar(String nombre, Long categoriaId, Pageable pageable) {
        if (categoriaId != null) {
            return productoRepository.findByCategoriaIdAndActivoTrueAndNombreContainingIgnoreCase(categoriaId, nombre, pageable);
        }
        return productoRepository.findByActivoTrueAndNombreContainingIgnoreCase(nombre, pageable);
    }

    public List<Producto> listarDestacados() {
        return productoRepository.findByActivoTrueAndDestacadoTrue();
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));

        producto.setActivo(false);
        productoRepository.save(producto);
    }
}

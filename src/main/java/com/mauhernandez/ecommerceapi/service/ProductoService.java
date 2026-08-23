package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

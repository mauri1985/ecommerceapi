package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.CarritoItem;
import com.mauhernandez.ecommerceapi.model.Producto;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.repository.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    private final CarritoItemRepository carritoItemRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    @Autowired
    public CarritoService(CarritoItemRepository carritoItemRepository,
                          ProductoService productoService,
                          UsuarioService usuarioService) {
        this.carritoItemRepository = carritoItemRepository;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    public List<CarritoItem> listarPorUsuario(Long usuarioId) {
        return carritoItemRepository.findByUsuarioId(usuarioId);
    }

    public CarritoItem agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {
        Producto producto = productoService.buscarPorId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));

        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId));

        return carritoItemRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .map(item -> {
                    item.setCantidad(item.getCantidad() + cantidad);
                    return carritoItemRepository.save(item);
                })
                .orElseGet(() -> {
                    CarritoItem nuevo = new CarritoItem();
                    nuevo.setUsuario(usuario);
                    nuevo.setProducto(producto);
                    nuevo.setCantidad(cantidad);
                    return carritoItemRepository.save(nuevo);
                });
    }

    public CarritoItem actualizarCantidad(Long carritoItemId, Integer cantidad) {
        CarritoItem item = carritoItemRepository.findById(carritoItemId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ítem de carrito no encontrado con id: " + carritoItemId));

        item.setCantidad(cantidad);
        return carritoItemRepository.save(item);
    }

    public void quitarItem(Long carritoItemId) {
        carritoItemRepository.deleteById(carritoItemId);
    }

    public void vaciarCarrito(Long usuarioId) {
        carritoItemRepository.deleteByUsuarioId(usuarioId);
    }
}
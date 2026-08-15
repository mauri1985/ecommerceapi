package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.ConflictoDeNegocioException;
import com.mauhernandez.ecommerceapi.model.*;
import com.mauhernandez.ecommerceapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoService carritoService;
    private final ProductoService productoService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         CarritoService carritoService,
                         ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    @Transactional
    public Pedido crearDesdeCarrito(Usuario usuario) {
        List<CarritoItem> itemsCarrito = carritoService.listarPorUsuario(usuario.getId());

        if (itemsCarrito.isEmpty()) {
            throw new ConflictoDeNegocioException("El carrito está vacío");
        }

        // Validar stock de todos los items ANTES de tocar nada
        for (CarritoItem item : itemsCarrito) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new ConflictoDeNegocioException(
                        "Stock insuficiente para el producto '" + producto.getNombre() +
                                "'. Disponible: " + producto.getStock() + ", solicitado: " + item.getCantidad()
                );
            }
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : itemsCarrito) {
            Producto producto = item.getProducto();

            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setPedido(pedido);
            pedidoItem.setProducto(producto);
            pedidoItem.setCantidad(item.getCantidad());
            pedidoItem.setPrecioUnitario(producto.getPrecio());

            pedido.getItems().add(pedidoItem);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));

            // Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.guardar(producto);
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);

        carritoService.vaciarCarrito(usuario.getId());

        return guardado;
    }
}
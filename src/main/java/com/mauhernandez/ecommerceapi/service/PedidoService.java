package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.ConflictoDeNegocioException;
import com.mauhernandez.ecommerceapi.model.*;
import com.mauhernandez.ecommerceapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            BigDecimal precioAUsar = (producto.getPrecioOferta() != null && producto.getPrecioOferta().compareTo(producto.getPrecio()) < 0)
                    ? producto.getPrecioOferta()
                    : producto.getPrecio();
            pedidoItem.setPrecioUnitario(precioAUsar);

            pedido.getItems().add(pedidoItem);

            total = total.add(precioAUsar.multiply(BigDecimal.valueOf(item.getCantidad())));

            // Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.guardar(producto);
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);

        carritoService.vaciarCarrito(usuario.getId());

        return guardado;
    }

    @Transactional
    public void restaurarStock(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null || pedido.getEstado() == Pedido.Estado.CANCELADO) {
            return; // ya cancelado antes, no restauramos dos veces
        }

        for (PedidoItem item : pedido.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productoService.guardar(producto);
        }
    }

    @Scheduled(fixedRate = 15 * 60 * 1000) // corre cada 15 minutos
    @Transactional
    public void cancelarPedidosAbandonados() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(30);

        List<Pedido> pendientesViejos = pedidoRepository.findByEstadoAndFechaBefore(Pedido.Estado.PENDIENTE, limite);

        for (Pedido pedido : pendientesViejos) {
            restaurarStock(pedido.getId());
            pedido.setEstado(Pedido.Estado.CANCELADO);
            pedidoRepository.save(pedido);
        }
    }
}
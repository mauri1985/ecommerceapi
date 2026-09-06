package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Pedido> findByEstadoAndFechaBefore(Pedido.Estado estado, LocalDateTime fecha);
}

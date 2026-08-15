package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.dto.PedidoMapper;
import com.mauhernandez.ecommerceapi.dto.PedidoResponse;
import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.service.PedidoService;
import com.mauhernandez.ecommerceapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final PedidoMapper pedidoMapper;

    @Autowired
    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.pedidoMapper = pedidoMapper;
    }

    @GetMapping("/{usuarioId}")
    public List<PedidoResponse> listar(@PathVariable Long usuarioId) {
        return pedidoService.listarPorUsuario(usuarioId).stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<PedidoResponse> crear(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId));

        var pedido = pedidoService.crearDesdeCarrito(usuario);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }
}
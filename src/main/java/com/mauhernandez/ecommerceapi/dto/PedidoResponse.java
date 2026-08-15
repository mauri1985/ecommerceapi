package com.mauhernandez.ecommerceapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        LocalDateTime fecha,
        String estado,
        BigDecimal total,
        List<PedidoItemResponse> items
) {}
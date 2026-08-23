package com.mauhernandez.ecommerceapi.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> contenido,
        int paginaActual,
        int totalPaginas,
        long totalElementos
) {}
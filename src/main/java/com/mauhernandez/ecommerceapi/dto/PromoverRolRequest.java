package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.NotNull;

public record PromoverRolRequest(
        @NotNull(message = "El rol es obligatorio")
        String rol
) {}
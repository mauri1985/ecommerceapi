package com.mauhernandez.ecommerceapi.dto;

import jakarta.validation.constraints.NotBlank;

public record SolicitarResetRequest(@NotBlank String email) {}

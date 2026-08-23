package com.mauhernandez.ecommerceapi.dto;

public record LoginResponse(Long id, String token, String email, String nombre, String rol) {}
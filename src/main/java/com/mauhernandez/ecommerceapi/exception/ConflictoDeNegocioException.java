package com.mauhernandez.ecommerceapi.exception;

public class ConflictoDeNegocioException extends RuntimeException {
    public ConflictoDeNegocioException(String mensaje) {
        super(mensaje);
    }
}

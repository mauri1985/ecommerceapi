package com.mauhernandez.ecommerceapi.exception;

import com.mauhernandez.ecommerceapi.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex) {
        List<String> mensajes = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        return construirRespuesta(HttpStatus.BAD_REQUEST, "Error de validación", mensajes);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrado(RecursoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", List.of(ex.getMessage()));
    }

    @ExceptionHandler(ConflictoDeNegocioException.class)
    public ResponseEntity<ErrorResponse> handleConflicto(ConflictoDeNegocioException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, "Conflicto", List.of(ex.getMessage()));
    }

    // Fallback para cualquier otro error inesperado
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Error", List.of(ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleCredencialesInvalidas(org.springframework.security.authentication.BadCredentialsException ex) {
        return construirRespuesta(HttpStatus.UNAUTHORIZED, "No autorizado", List.of(ex.getMessage()));
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(HttpStatus status, String error, List<String> mensajes) {
        ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(), error, mensajes);
        return ResponseEntity.status(status).body(body);
    }
}
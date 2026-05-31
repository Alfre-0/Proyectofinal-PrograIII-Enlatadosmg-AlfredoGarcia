package com.alfredogarcia.proyectofinal.enlatadosmg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> manejarRuntimeException(RuntimeException ex) {
        String mensaje = ex.getMessage() != null ? ex.getMessage() : "Ocurrió un error inesperado";
        return ResponseEntity.badRequest().body("{\"error\": \"" + mensaje.replace("\"", "\\\"") + "\"}");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarException(Exception ex) {
        String mensaje = ex.getMessage() != null ? ex.getMessage() : "Error interno del servidor";
        return ResponseEntity.internalServerError().body("{\"error\": \"Error del sistema: " + mensaje.replace("\"", "\\\"") + "\"}");
    }
}

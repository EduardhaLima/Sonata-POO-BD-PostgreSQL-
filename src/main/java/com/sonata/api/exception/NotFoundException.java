package com.sonata.api.exception;

/**
 * Lancada quando um recurso pedido nao existe.
 * O {@link GlobalExceptionHandler} converte em HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

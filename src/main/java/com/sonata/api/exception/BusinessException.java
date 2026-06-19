package com.sonata.api.exception;

/**
 * Erro de regra de negocio (ex.: playlist cheia, movimento duplicado).
 * Convertido em HTTP 422 pelo {@link GlobalExceptionHandler}.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

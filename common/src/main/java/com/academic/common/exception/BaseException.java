package com.academic.common.exception;

import lombok.Getter;

import java.util.List;

/**
 * Excepción base para todas las excepciones personalizadas del sistema.
 * Proporciona estructura común con código de error y detalles.
 */
@Getter
public abstract class BaseException extends RuntimeException {

    private final String errorCode;
    private final List<String> details;

    protected BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.details = List.of();
    }

    protected BaseException(String message, String errorCode, List<String> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details != null ? details : List.of();
    }

    protected BaseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = List.of();
    }
}

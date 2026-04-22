package com.academic.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estándar de respuesta de error para todos los microservicios.
 * Cumple con el formato requerido en las especificaciones del proyecto.
 */
@Getter
@Builder
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    private final String path;

    private final String errorCode;

    private final String message;

    private final List<String> details;

    /**
     * Factory method para crear una respuesta de error.
     *
     * @param path      URI del endpoint que generó el error
     * @param errorCode Código de error único
     * @param message   Mensaje descriptivo del error
     * @param details   Detalles adicionales (campos con error, IDs, etc.)
     * @return Instancia de ErrorResponse
     */
    public static ErrorResponse of(String path, String errorCode, String message, List<String> details) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .path(path)
                .errorCode(errorCode)
                .message(message)
                .details(details != null ? details : List.of())
                .build();
    }

    /**
     * Factory method simplificado sin detalles adicionales.
     *
     * @param path      URI del endpoint que generó el error
     * @param errorCode Código de error único
     * @param message   Mensaje descriptivo del error
     * @return Instancia de ErrorResponse
     */
    public static ErrorResponse of(String path, String errorCode, String message) {
        return of(path, errorCode, message, List.of());
    }
}

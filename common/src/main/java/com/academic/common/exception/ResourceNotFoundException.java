package com.academic.common.exception;

import java.util.List;

/**
 * Excepción lanzada cuando un recurso solicitado no existe.
 * Mapea a HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }

    public ResourceNotFoundException(String message, String errorCode, List<String> details) {
        super(message, errorCode, details);
    }

    /**
     * Factory method para crear excepción de recurso no encontrado.
     *
     * @param resourceName Nombre del recurso (Student, Course, etc.)
     * @param fieldName    Nombre del campo usado para buscar
     * @param fieldValue   Valor del campo
     * @return Excepción configurada
     */
    public static ResourceNotFoundException of(String resourceName, String fieldName, Object fieldValue) {
        String errorCode = resourceName.toUpperCase() + "_NOT_FOUND";
        String message = String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue);
        List<String> details = List.of(fieldName + "=" + fieldValue);
        return new ResourceNotFoundException(message, errorCode, details);
    }
}

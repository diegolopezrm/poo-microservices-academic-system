package com.academic.common.exception;

import java.util.List;

/**
 * Excepción lanzada cuando existe un conflicto con el estado actual del recurso.
 * Mapea a HTTP 409 Conflict.
 */
public class ConflictException extends BaseException {

    public ConflictException(String message, String errorCode) {
        super(message, errorCode);
    }

    public ConflictException(String message, String errorCode, List<String> details) {
        super(message, errorCode, details);
    }

    /**
     * Factory method para matrícula duplicada.
     *
     * @param studentId ID del estudiante
     * @param courseId  ID del curso
     * @return Excepción configurada
     */
    public static ConflictException enrollmentConflict(Long studentId, Long courseId) {
        return new ConflictException(
                "Student already enrolled in this course",
                "ENROLLMENT_CONFLICT",
                List.of("studentId=" + studentId, "courseId=" + courseId)
        );
    }

    /**
     * Factory method para email duplicado.
     *
     * @param email Email duplicado
     * @return Excepción configurada
     */
    public static ConflictException emailAlreadyExists(String email) {
        return new ConflictException(
                "Email already exists",
                "EMAIL_CONFLICT",
                List.of("email=" + email)
        );
    }
}

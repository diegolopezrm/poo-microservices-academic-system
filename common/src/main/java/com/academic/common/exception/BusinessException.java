package com.academic.common.exception;

import java.util.List;

/**
 * Excepción lanzada cuando se viola una regla de negocio.
 * Mapea a HTTP 422 Unprocessable Entity.
 */
public class BusinessException extends BaseException {

    public BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }

    public BusinessException(String message, String errorCode, List<String> details) {
        super(message, errorCode, details);
    }

    /**
     * Factory method para estudiante inactivo.
     *
     * @param studentId ID del estudiante
     * @return Excepción configurada
     */
    public static BusinessException studentInactive(Long studentId) {
        return new BusinessException(
                "Student is not active",
                "STUDENT_INACTIVE",
                List.of("studentId=" + studentId)
        );
    }

    /**
     * Factory method para curso inactivo.
     *
     * @param courseId ID del curso
     * @return Excepción configurada
     */
    public static BusinessException courseInactive(Long courseId) {
        return new BusinessException(
                "Course is not active",
                "COURSE_INACTIVE",
                List.of("courseId=" + courseId)
        );
    }

    /**
     * Factory method para curso sin cupo disponible.
     *
     * @param courseId ID del curso
     * @return Excepción configurada
     */
    public static BusinessException courseFull(Long courseId) {
        return new BusinessException(
                "Course has no available capacity",
                "COURSE_FULL",
                List.of("courseId=" + courseId)
        );
    }
}

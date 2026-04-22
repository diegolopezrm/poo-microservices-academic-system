package com.academic.matriculas.validation;

import com.academic.matriculas.domain.dto.CourseDTO;
import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.StudentDTO;

/**
 * Interfaz para el patrón Strategy de validación de matrículas.
 * Cada implementación representa una regla de negocio específica.
 *
 * <p>Principios aplicados:</p>
 * <ul>
 *   <li><b>SRP</b>: Cada estrategia tiene una única responsabilidad de validación</li>
 *   <li><b>OCP</b>: Nuevas validaciones se agregan creando nuevas implementaciones</li>
 *   <li><b>LSP</b>: Todas las estrategias son intercambiables</li>
 *   <li><b>DIP</b>: El servicio depende de esta abstracción, no de implementaciones</li>
 * </ul>
 */
public interface EnrollmentValidationStrategy {

    /**
     * Ejecuta la validación.
     *
     * @param request   datos de la matrícula a crear
     * @param student   datos del estudiante (puede ser null si aún no se ha obtenido)
     * @param course    datos del curso (puede ser null si aún no se ha obtenido)
     * @throws com.academic.common.exception.BaseException si la validación falla
     */
    void validate(EnrollmentCreateRequest request, StudentDTO student, CourseDTO course);

    /**
     * Retorna el orden de ejecución de esta validación.
     * Las validaciones se ejecutan en orden ascendente.
     *
     * @return número de orden
     */
    int getOrder();

    /**
     * Indica si esta validación requiere datos del estudiante.
     *
     * @return true si requiere StudentDTO
     */
    default boolean requiresStudent() {
        return false;
    }

    /**
     * Indica si esta validación requiere datos del curso.
     *
     * @return true si requiere CourseDTO
     */
    default boolean requiresCourse() {
        return false;
    }
}

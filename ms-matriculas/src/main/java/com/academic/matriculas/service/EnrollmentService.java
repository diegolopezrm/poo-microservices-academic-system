package com.academic.matriculas.service;

import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.EnrollmentResponse;

import java.util.List;

/**
 * Interfaz del servicio de matrículas.
 * Define las operaciones disponibles siguiendo el principio ISP.
 */
public interface EnrollmentService {

    /**
     * Obtiene todas las matrículas.
     *
     * @return lista de matrículas
     */
    List<EnrollmentResponse> getAllEnrollments();

    /**
     * Crea una nueva matrícula en el sistema.
     * Aplica todas las validaciones de negocio requeridas.
     *
     * @param request datos de la matrícula a crear
     * @return datos de la matrícula creada
     */
    EnrollmentResponse createEnrollment(EnrollmentCreateRequest request);

    /**
     * Obtiene una matrícula por su ID.
     *
     * @param id ID de la matrícula
     * @return datos de la matrícula
     */
    EnrollmentResponse getEnrollmentById(Long id);

    /**
     * Busca matrículas por ID de estudiante.
     *
     * @param studentId ID del estudiante
     * @return lista de matrículas del estudiante
     */
    List<EnrollmentResponse> getEnrollmentsByStudentId(Long studentId);

    /**
     * Busca matrículas por ID de curso.
     *
     * @param courseId ID del curso
     * @return lista de matrículas en el curso
     */
    List<EnrollmentResponse> getEnrollmentsByCourseId(Long courseId);
}

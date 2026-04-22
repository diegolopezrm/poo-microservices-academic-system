package com.academic.matriculas.repository;

import com.academic.matriculas.domain.entity.Enrollment;
import com.academic.matriculas.domain.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de persistencia de Enrollment.
 * Implementa el patrón Repository a través de Spring Data JPA.
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Busca matrículas por ID de estudiante.
     *
     * @param studentId ID del estudiante
     * @return lista de matrículas del estudiante
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * Busca matrículas por ID de curso.
     *
     * @param courseId ID del curso
     * @return lista de matrículas en el curso
     */
    List<Enrollment> findByCourseId(Long courseId);

    /**
     * Verifica si existe una matrícula activa para el estudiante y curso dados.
     *
     * @param studentId ID del estudiante
     * @param courseId  ID del curso
     * @param status    estado de la matrícula
     * @return true si existe, false en caso contrario
     */
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    /**
     * Busca matrículas por estado.
     *
     * @param status estado a buscar
     * @return lista de matrículas con el estado dado
     */
    List<Enrollment> findByStatus(EnrollmentStatus status);
}

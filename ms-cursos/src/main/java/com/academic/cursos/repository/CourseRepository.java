package com.academic.cursos.repository;

import com.academic.cursos.domain.entity.Course;
import com.academic.cursos.domain.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de persistencia de Course.
 * Implementa el patrón Repository a través de Spring Data JPA.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Busca cursos por su estado.
     *
     * @param status estado a buscar
     * @return lista de cursos con el estado dado
     */
    List<Course> findByStatus(CourseStatus status);

    /**
     * Busca cursos que tengan cupo disponible.
     *
     * @return lista de cursos con cupo disponible
     */
    @Query("SELECT c FROM Course c WHERE c.enrolledCount < c.capacity AND c.status = 'ACTIVE'")
    List<Course> findAvailableCourses();

    /**
     * Incrementa el contador de matriculados de un curso.
     *
     * @param courseId ID del curso
     * @return número de filas afectadas
     */
    @Modifying
    @Query("UPDATE Course c SET c.enrolledCount = c.enrolledCount + 1 WHERE c.id = :courseId AND c.enrolledCount < c.capacity")
    int incrementEnrolledCount(@Param("courseId") Long courseId);

    /**
     * Decrementa el contador de matriculados de un curso.
     *
     * @param courseId ID del curso
     * @return número de filas afectadas
     */
    @Modifying
    @Query("UPDATE Course c SET c.enrolledCount = c.enrolledCount - 1 WHERE c.id = :courseId AND c.enrolledCount > 0")
    int decrementEnrolledCount(@Param("courseId") Long courseId);
}

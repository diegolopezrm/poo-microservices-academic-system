package com.academic.estudiantes.repository;

import com.academic.estudiantes.domain.entity.Student;
import com.academic.estudiantes.domain.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de Student.
 * Implementa el patrón Repository a través de Spring Data JPA.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Busca un estudiante por su email.
     *
     * @param email email del estudiante
     * @return Optional con el estudiante si existe
     */
    Optional<Student> findByEmail(String email);

    /**
     * Verifica si existe un estudiante con el email dado.
     *
     * @param email email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca estudiantes por su estado.
     *
     * @param status estado a buscar
     * @return lista de estudiantes con el estado dado
     */
    List<Student> findByStatus(StudentStatus status);
}

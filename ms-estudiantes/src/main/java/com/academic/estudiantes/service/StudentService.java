package com.academic.estudiantes.service;

import com.academic.estudiantes.domain.dto.StudentCreateRequest;
import com.academic.estudiantes.domain.dto.StudentResponse;
import com.academic.estudiantes.domain.dto.StudentStatusRequest;

import java.util.List;

/**
 * Interfaz del servicio de estudiantes.
 * Define las operaciones disponibles siguiendo el principio ISP.
 */
public interface StudentService {

    /**
     * Obtiene todos los estudiantes.
     *
     * @return lista de estudiantes
     */
    List<StudentResponse> getAllStudents();

    /**
     * Crea un nuevo estudiante en el sistema.
     *
     * @param request datos del estudiante a crear
     * @return datos del estudiante creado
     */
    StudentResponse createStudent(StudentCreateRequest request);

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param id ID del estudiante
     * @return datos del estudiante
     */
    StudentResponse getStudentById(Long id);

    /**
     * Actualiza el estado de un estudiante.
     *
     * @param id      ID del estudiante
     * @param request nuevo estado
     * @return datos del estudiante actualizado
     */
    StudentResponse updateStatus(Long id, StudentStatusRequest request);
}

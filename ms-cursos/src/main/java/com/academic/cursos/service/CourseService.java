package com.academic.cursos.service;

import com.academic.cursos.domain.dto.CourseCapacityRequest;
import com.academic.cursos.domain.dto.CourseCreateRequest;
import com.academic.cursos.domain.dto.CourseResponse;
import com.academic.cursos.domain.dto.CourseStatusRequest;

import java.util.List;

/**
 * Interfaz del servicio de cursos.
 * Define las operaciones disponibles siguiendo el principio ISP.
 */
public interface CourseService {

    /**
     * Obtiene todos los cursos.
     *
     * @return lista de cursos
     */
    List<CourseResponse> getAllCourses();

    /**
     * Crea un nuevo curso en el sistema.
     *
     * @param request datos del curso a crear
     * @return datos del curso creado
     */
    CourseResponse createCourse(CourseCreateRequest request);

    /**
     * Obtiene un curso por su ID.
     *
     * @param id ID del curso
     * @return datos del curso
     */
    CourseResponse getCourseById(Long id);

    /**
     * Actualiza el estado de un curso.
     *
     * @param id      ID del curso
     * @param request nuevo estado
     * @return datos del curso actualizado
     */
    CourseResponse updateStatus(Long id, CourseStatusRequest request);

    /**
     * Actualiza la capacidad de un curso.
     *
     * @param id      ID del curso
     * @param request nueva capacidad
     * @return datos del curso actualizado
     */
    CourseResponse updateCapacity(Long id, CourseCapacityRequest request);
}

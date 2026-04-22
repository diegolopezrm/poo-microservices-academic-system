package com.academic.cursos.controller;

import com.academic.cursos.domain.dto.CourseCapacityRequest;
import com.academic.cursos.domain.dto.CourseCreateRequest;
import com.academic.cursos.domain.dto.CourseResponse;
import com.academic.cursos.domain.dto.CourseStatusRequest;
import com.academic.cursos.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de cursos.
 * Sigue el principio SRP manejando únicamente requests HTTP.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * Obtiene todos los cursos.
     *
     * @return lista de cursos con HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        log.info("GET /api/v1/courses - Fetching all courses");
        List<CourseResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un nuevo curso.
     *
     * @param request datos del curso
     * @return curso creado con HTTP 201
     */
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseCreateRequest request) {

        log.info("POST /api/v1/courses - Creating course: {}", request.getName());

        CourseResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene un curso por su ID.
     *
     * @param id ID del curso
     * @return datos del curso con HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {

        log.info("GET /api/v1/courses/{} - Fetching course", id);

        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza el estado de un curso.
     *
     * @param id      ID del curso
     * @param request nuevo estado
     * @return curso actualizado con HTTP 200
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<CourseResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody CourseStatusRequest request) {

        log.info("PATCH /api/v1/courses/{}/status - Updating to {}", id, request.getStatus());

        CourseResponse response = courseService.updateStatus(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza la capacidad de un curso.
     *
     * @param id      ID del curso
     * @param request nueva capacidad
     * @return curso actualizado con HTTP 200
     */
    @PatchMapping("/{id}/capacity")
    public ResponseEntity<CourseResponse> updateCapacity(
            @PathVariable Long id,
            @Valid @RequestBody CourseCapacityRequest request) {

        log.info("PATCH /api/v1/courses/{}/capacity - Updating to {}", id, request.getCapacity());

        CourseResponse response = courseService.updateCapacity(id, request);
        return ResponseEntity.ok(response);
    }
}

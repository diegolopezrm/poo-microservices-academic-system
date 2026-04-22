package com.academic.matriculas.controller;

import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.EnrollmentResponse;
import com.academic.matriculas.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de matrículas.
 * Sigue el principio SRP manejando únicamente requests HTTP.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * Obtiene todas las matrículas o busca por estudiante/curso.
     *
     * @param studentId ID del estudiante (opcional)
     * @param courseId  ID del curso (opcional)
     * @return lista de matrículas con HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getEnrollments(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId) {

        List<EnrollmentResponse> response;

        if (studentId != null) {
            log.info("GET /api/v1/enrollments?studentId={} - Searching by student", studentId);
            response = enrollmentService.getEnrollmentsByStudentId(studentId);
        } else if (courseId != null) {
            log.info("GET /api/v1/enrollments?courseId={} - Searching by course", courseId);
            response = enrollmentService.getEnrollmentsByCourseId(courseId);
        } else {
            log.info("GET /api/v1/enrollments - Fetching all enrollments");
            response = enrollmentService.getAllEnrollments();
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Crea una nueva matrícula.
     *
     * @param request datos de la matrícula
     * @return matrícula creada con HTTP 201
     */
    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody EnrollmentCreateRequest request) {

        log.info("POST /api/v1/enrollments - Creating enrollment for student {} in course {}",
                request.getStudentId(), request.getCourseId());

        EnrollmentResponse response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene una matrícula por su ID.
     *
     * @param id ID de la matrícula
     * @return datos de la matrícula con HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {

        log.info("GET /api/v1/enrollments/{} - Fetching enrollment", id);

        EnrollmentResponse response = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(response);
    }
}

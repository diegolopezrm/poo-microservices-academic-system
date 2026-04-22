package com.academic.estudiantes.controller;

import com.academic.estudiantes.domain.dto.StudentCreateRequest;
import com.academic.estudiantes.domain.dto.StudentResponse;
import com.academic.estudiantes.domain.dto.StudentStatusRequest;
import com.academic.estudiantes.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de estudiantes.
 * Sigue el principio SRP manejando únicamente requests HTTP.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * Obtiene todos los estudiantes.
     *
     * @return lista de estudiantes con HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        log.info("GET /api/v1/students - Fetching all students");
        List<StudentResponse> response = studentService.getAllStudents();
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un nuevo estudiante.
     *
     * @param request datos del estudiante
     * @return estudiante creado con HTTP 201
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentCreateRequest request) {

        log.info("POST /api/v1/students - Creating student with email: {}", request.getEmail());

        StudentResponse response = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param id ID del estudiante
     * @return datos del estudiante con HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {

        log.info("GET /api/v1/students/{} - Fetching student", id);

        StudentResponse response = studentService.getStudentById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza el estado de un estudiante.
     *
     * @param id      ID del estudiante
     * @param request nuevo estado
     * @return estudiante actualizado con HTTP 200
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StudentStatusRequest request) {

        log.info("PATCH /api/v1/students/{}/status - Updating to {}", id, request.getStatus());

        StudentResponse response = studentService.updateStatus(id, request);
        return ResponseEntity.ok(response);
    }
}

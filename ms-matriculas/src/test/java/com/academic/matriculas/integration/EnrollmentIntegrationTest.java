package com.academic.matriculas.integration;

import com.academic.matriculas.domain.entity.Enrollment;
import com.academic.matriculas.domain.enums.EnrollmentStatus;
import com.academic.matriculas.repository.EnrollmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para el microservicio de matrículas.
 * Prueba los endpoints que no requieren comunicación con otros servicios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EnrollmentIntegrationTest {

    private static final String BASE_URL = "/api/v1/enrollments";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/v1/enrollments/{id} - Obtener matrícula por ID")
    void getEnrollmentById_Success() throws Exception {
        // Crear matrícula directamente en el repositorio
        Enrollment enrollment = Enrollment.builder()
                .studentId(1L)
                .courseId(1L)
                .status(EnrollmentStatus.ACTIVE)
                .build();
        Enrollment saved = enrollmentRepository.save(enrollment);

        mockMvc.perform(get(BASE_URL + "/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("GET /api/v1/enrollments/{id} - ID no existente retorna 404")
    void getEnrollmentById_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ENROLLMENT_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/v1/enrollments?studentId={id} - Obtener matrículas por estudiante")
    void getEnrollmentsByStudentId_Success() throws Exception {
        // Crear matrículas para diferentes estudiantes
        enrollmentRepository.save(Enrollment.builder()
                .studentId(1L).courseId(1L).status(EnrollmentStatus.ACTIVE).build());
        enrollmentRepository.save(Enrollment.builder()
                .studentId(1L).courseId(2L).status(EnrollmentStatus.ACTIVE).build());
        enrollmentRepository.save(Enrollment.builder()
                .studentId(2L).courseId(1L).status(EnrollmentStatus.ACTIVE).build());

        mockMvc.perform(get(BASE_URL).param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].studentId", everyItem(equalTo(1))));
    }

    @Test
    @DisplayName("GET /api/v1/enrollments?courseId={id} - Obtener matrículas por curso")
    void getEnrollmentsByCourseId_Success() throws Exception {
        // Crear matrículas para diferentes cursos
        enrollmentRepository.save(Enrollment.builder()
                .studentId(1L).courseId(1L).status(EnrollmentStatus.ACTIVE).build());
        enrollmentRepository.save(Enrollment.builder()
                .studentId(2L).courseId(1L).status(EnrollmentStatus.ACTIVE).build());
        enrollmentRepository.save(Enrollment.builder()
                .studentId(3L).courseId(2L).status(EnrollmentStatus.ACTIVE).build());

        mockMvc.perform(get(BASE_URL).param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].courseId", everyItem(equalTo(1))));
    }

    @Test
    @DisplayName("GET /api/v1/enrollments - Sin parámetros retorna lista vacía")
    void getEnrollmentsWithoutParams_ReturnsEmpty() throws Exception {
        // Crear algunas matrículas
        enrollmentRepository.save(Enrollment.builder()
                .studentId(1L).courseId(1L).status(EnrollmentStatus.ACTIVE).build());

        // Sin parámetros retorna lista vacía según la lógica del controlador
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/v1/enrollments - Estudiante sin matrículas retorna lista vacía")
    void getEnrollmentsByStudentId_Empty() throws Exception {
        mockMvc.perform(get(BASE_URL).param("studentId", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

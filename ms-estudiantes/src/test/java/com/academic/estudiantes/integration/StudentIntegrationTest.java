package com.academic.estudiantes.integration;

import com.academic.estudiantes.domain.dto.StudentCreateRequest;
import com.academic.estudiantes.domain.dto.StudentResponse;
import com.academic.estudiantes.domain.dto.StudentStatusRequest;
import com.academic.estudiantes.domain.enums.StudentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para el microservicio de estudiantes.
 * Utiliza H2 in-memory y prueba el flujo completo REST → Service → Repository.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentIntegrationTest {

    private static final String BASE_URL = "/api/v1/students";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/students - Crear estudiante exitosamente")
    void createStudent_Success() throws Exception {
        StudentCreateRequest request = StudentCreateRequest.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@university.edu")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Pérez"))
                .andExpect(jsonPath("$.email").value("juan.perez@university.edu"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("POST /api/v1/students - Email duplicado retorna 409")
    void createStudent_DuplicateEmail_Conflict() throws Exception {
        StudentCreateRequest request = StudentCreateRequest.builder()
                .firstName("María")
                .lastName("García")
                .email("maria.garcia@university.edu")
                .build();

        // Crear primer estudiante
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Intentar crear con el mismo email
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("EMAIL_CONFLICT"));
    }

    @Test
    @DisplayName("GET /api/v1/students/{id} - Obtener estudiante por ID")
    void getStudentById_Success() throws Exception {
        // Crear estudiante
        StudentCreateRequest request = StudentCreateRequest.builder()
                .firstName("Carlos")
                .lastName("López")
                .email("carlos.lopez@university.edu")
                .build();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        StudentResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), StudentResponse.class);

        // Obtener por ID
        mockMvc.perform(get(BASE_URL + "/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.firstName").value("Carlos"))
                .andExpect(jsonPath("$.lastName").value("López"));
    }

    @Test
    @DisplayName("GET /api/v1/students/{id} - ID no existente retorna 404")
    void getStudentById_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("STUDENT_NOT_FOUND"));
    }

    @Test
    @DisplayName("PATCH /api/v1/students/{id}/status - Cambiar estado a INACTIVE")
    void updateStudentStatus_Success() throws Exception {
        // Crear estudiante
        StudentCreateRequest createRequest = StudentCreateRequest.builder()
                .firstName("Laura")
                .lastName("Torres")
                .email("laura.torres@university.edu")
                .build();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        StudentResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), StudentResponse.class);

        // Cambiar estado con PATCH
        StudentStatusRequest statusRequest = StudentStatusRequest.builder()
                .status(StudentStatus.INACTIVE)
                .build();

        mockMvc.perform(patch(BASE_URL + "/{id}/status", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }
}

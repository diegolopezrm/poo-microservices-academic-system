package com.academic.cursos.integration;

import com.academic.cursos.domain.dto.CourseCapacityRequest;
import com.academic.cursos.domain.dto.CourseCreateRequest;
import com.academic.cursos.domain.dto.CourseResponse;
import com.academic.cursos.domain.dto.CourseStatusRequest;
import com.academic.cursos.domain.enums.CourseStatus;
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
 * Tests de integración para el microservicio de cursos.
 * Utiliza H2 in-memory y prueba el flujo completo REST → Service → Repository.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CourseIntegrationTest {

    private static final String BASE_URL = "/api/v1/courses";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/courses - Crear curso exitosamente")
    void createCourse_Success() throws Exception {
        CourseCreateRequest request = CourseCreateRequest.builder()
                .name("Programación Orientada a Objetos")
                .description("Curso de POO con Java")
                .capacity(30)
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Programación Orientada a Objetos"))
                .andExpect(jsonPath("$.description").value("Curso de POO con Java"))
                .andExpect(jsonPath("$.capacity").value(30))
                .andExpect(jsonPath("$.enrolledCount").value(0))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("POST /api/v1/courses - Crear múltiples cursos exitosamente")
    void createMultipleCourses_Success() throws Exception {
        CourseCreateRequest request1 = CourseCreateRequest.builder()
                .name("Estructuras de Datos")
                .description("Curso de estructuras de datos")
                .capacity(25)
                .build();

        CourseCreateRequest request2 = CourseCreateRequest.builder()
                .name("Algoritmos")
                .description("Curso de algoritmos")
                .capacity(30)
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Estructuras de Datos"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Algoritmos"));
    }

    @Test
    @DisplayName("GET /api/v1/courses/{id} - Obtener curso por ID")
    void getCourseById_Success() throws Exception {
        CourseCreateRequest request = CourseCreateRequest.builder()
                .name("Bases de Datos")
                .description("Fundamentos de bases de datos")
                .capacity(35)
                .build();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        CourseResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), CourseResponse.class);

        mockMvc.perform(get(BASE_URL + "/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.name").value("Bases de Datos"))
                .andExpect(jsonPath("$.description").value("Fundamentos de bases de datos"));
    }

    @Test
    @DisplayName("GET /api/v1/courses/{id} - ID no existente retorna 404")
    void getCourseById_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("COURSE_NOT_FOUND"));
    }

    @Test
    @DisplayName("PATCH /api/v1/courses/{id}/status - Cambiar estado a INACTIVE")
    void updateCourseStatus_Success() throws Exception {
        CourseCreateRequest createRequest = CourseCreateRequest.builder()
                .name("Sistemas Operativos")
                .description("Fundamentos de SO")
                .capacity(30)
                .build();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        CourseResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), CourseResponse.class);

        CourseStatusRequest statusRequest = CourseStatusRequest.builder()
                .status(CourseStatus.INACTIVE)
                .build();

        mockMvc.perform(patch(BASE_URL + "/{id}/status", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    @DisplayName("PATCH /api/v1/courses/{id}/capacity - Actualizar capacidad")
    void updateCourseCapacity_Success() throws Exception {
        CourseCreateRequest createRequest = CourseCreateRequest.builder()
                .name("Ingeniería de Software")
                .description("Metodologías de desarrollo")
                .capacity(25)
                .build();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        CourseResponse created = objectMapper.readValue(
                result.getResponse().getContentAsString(), CourseResponse.class);

        CourseCapacityRequest capacityRequest = CourseCapacityRequest.builder()
                .capacity(40)
                .build();

        mockMvc.perform(patch(BASE_URL + "/{id}/capacity", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(capacityRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value(40));
    }
}

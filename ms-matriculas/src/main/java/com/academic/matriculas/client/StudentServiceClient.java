package com.academic.matriculas.client;

import com.academic.common.exception.ResourceNotFoundException;
import com.academic.matriculas.domain.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente REST para comunicarse con el microservicio de estudiantes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StudentServiceClient {

    private final RestClient studentRestClient;

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param studentId ID del estudiante
     * @return datos del estudiante
     * @throws ResourceNotFoundException si el estudiante no existe
     */
    public StudentDTO getStudentById(Long studentId) {
        log.debug("Fetching student with ID: {} from ms-estudiantes", studentId);

        return studentRestClient.get()
                .uri("/students/{id}", studentId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.warn("Student not found with ID: {}", studentId);
                    throw ResourceNotFoundException.of("Student", "id", studentId);
                })
                .body(StudentDTO.class);
    }
}

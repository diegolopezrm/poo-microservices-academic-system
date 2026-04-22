package com.academic.matriculas.client;

import com.academic.common.exception.ResourceNotFoundException;
import com.academic.matriculas.domain.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente REST para comunicarse con el microservicio de cursos.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CourseServiceClient {

    private final RestClient courseRestClient;

    /**
     * Obtiene un curso por su ID.
     *
     * @param courseId ID del curso
     * @return datos del curso
     * @throws ResourceNotFoundException si el curso no existe
     */
    public CourseDTO getCourseById(Long courseId) {
        log.debug("Fetching course with ID: {} from ms-cursos", courseId);

        return courseRestClient.get()
                .uri("/courses/{id}", courseId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.warn("Course not found with ID: {}", courseId);
                    throw ResourceNotFoundException.of("Course", "id", courseId);
                })
                .body(CourseDTO.class);
    }
}

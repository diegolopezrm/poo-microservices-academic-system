package com.academic.cursos.service.impl;

import com.academic.common.exception.BusinessException;
import com.academic.common.exception.ResourceNotFoundException;
import com.academic.cursos.domain.dto.CourseCapacityRequest;
import com.academic.cursos.domain.dto.CourseCreateRequest;
import com.academic.cursos.domain.dto.CourseResponse;
import com.academic.cursos.domain.dto.CourseStatusRequest;
import com.academic.cursos.domain.entity.Course;
import com.academic.cursos.domain.enums.CourseStatus;
import com.academic.cursos.mapper.CourseMapper;
import com.academic.cursos.repository.CourseRepository;
import com.academic.cursos.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de cursos.
 * Contiene la lógica de negocio siguiendo el principio SRP.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseResponse> getAllCourses() {
        log.debug("Fetching all courses");
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        log.info("Creating course: {}", request.getName());

        // Crear y persistir el curso
        Course course = courseMapper.toEntity(request);
        course.setStatus(CourseStatus.ACTIVE);
        course.setEnrolledCount(0);

        Course savedCourse = courseRepository.save(course);
        log.info("Course created successfully with ID: {}", savedCourse.getId());

        return courseMapper.toResponse(savedCourse);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        log.debug("Fetching course with ID: {}", id);

        Course course = findCourseById(id);
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse updateStatus(Long id, CourseStatusRequest request) {
        log.info("Updating status of course {} to {}", id, request.getStatus());

        Course course = findCourseById(id);

        // Actualizar estado usando métodos de la entidad rica
        if (request.getStatus() == CourseStatus.ACTIVE) {
            course.activate();
        } else {
            course.deactivate();
        }

        Course updatedCourse = courseRepository.save(course);
        log.info("Course {} status updated to {}", id, updatedCourse.getStatus());

        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCapacity(Long id, CourseCapacityRequest request) {
        log.info("Updating capacity of course {} to {}", id, request.getCapacity());

        Course course = findCourseById(id);

        // Validar que la nueva capacidad no sea menor a los matriculados actuales
        if (request.getCapacity() < course.getEnrolledCount()) {
            log.warn("Cannot set capacity {} below enrolled count {} for course {}",
                    request.getCapacity(), course.getEnrolledCount(), id);
            throw new BusinessException(
                    "New capacity cannot be less than current enrolled count",
                    "INVALID_CAPACITY",
                    List.of("requestedCapacity=" + request.getCapacity(),
                            "currentEnrolled=" + course.getEnrolledCount())
            );
        }

        course.setCapacity(request.getCapacity());
        Course updatedCourse = courseRepository.save(course);
        log.info("Course {} capacity updated to {}", id, updatedCourse.getCapacity());

        return courseMapper.toResponse(updatedCourse);
    }

    /**
     * Método privado para buscar curso por ID o lanzar excepción.
     *
     * @param id ID del curso
     * @return entidad Course
     * @throws ResourceNotFoundException si el curso no existe
     */
    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Course not found with ID: {}", id);
                    return ResourceNotFoundException.of("Course", "id", id);
                });
    }
}

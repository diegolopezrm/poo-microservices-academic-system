package com.academic.matriculas.service.impl;

import com.academic.common.exception.ResourceNotFoundException;
import com.academic.matriculas.client.CourseServiceClient;
import com.academic.matriculas.client.StudentServiceClient;
import com.academic.matriculas.domain.dto.*;
import com.academic.matriculas.domain.entity.Enrollment;
import com.academic.matriculas.domain.enums.EnrollmentStatus;
import com.academic.matriculas.mapper.EnrollmentMapper;
import com.academic.matriculas.repository.EnrollmentRepository;
import com.academic.matriculas.service.EnrollmentService;
import com.academic.matriculas.validation.EnrollmentValidationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Implementación del servicio de matrículas.
 * Implementa el patrón Template Method para el proceso de matrícula
 * y utiliza el patrón Strategy para las validaciones.
 *
 * <p>Principios SOLID aplicados:</p>
 * <ul>
 *   <li><b>SRP</b>: Este servicio orquesta, las validaciones están separadas</li>
 *   <li><b>OCP</b>: Nuevas validaciones se agregan sin modificar este código</li>
 *   <li><b>DIP</b>: Depende de abstracciones (interfaces de validación y clientes)</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final StudentServiceClient studentServiceClient;
    private final CourseServiceClient courseServiceClient;
    private final List<EnrollmentValidationStrategy> validationStrategies;

    @Override
    public List<EnrollmentResponse> getAllEnrollments() {
        log.debug("Fetching all enrollments");
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    /**
     * Crea una matrícula siguiendo el patrón Template Method:
     * 1. Obtener datos de servicios externos
     * 2. Ejecutar validaciones (Strategy Pattern)
     * 3. Crear la matrícula
     * 4. Post-procesamiento
     */
    @Override
    @Transactional
    public EnrollmentResponse createEnrollment(EnrollmentCreateRequest request) {
        log.info("Starting enrollment process for student {} in course {}",
                request.getStudentId(), request.getCourseId());

        // Step 1: Obtener datos de servicios externos
        StudentDTO student = fetchStudentData(request.getStudentId());
        CourseDTO course = fetchCourseData(request.getCourseId());

        // Step 2: Ejecutar validaciones ordenadas (Strategy Pattern)
        executeValidations(request, student, course);

        // Step 3: Crear y persistir la matrícula
        Enrollment enrollment = createAndSaveEnrollment(request);

        // Step 4: Post-procesamiento (logging, eventos, etc.)
        postProcessEnrollment(enrollment, student, course);

        log.info("Enrollment created successfully with ID: {}", enrollment.getId());
        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    public EnrollmentResponse getEnrollmentById(Long id) {
        log.debug("Fetching enrollment with ID: {}", id);

        Enrollment enrollment = findEnrollmentById(id);
        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByStudentId(Long studentId) {
        log.debug("Fetching enrollments for student ID: {}", studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByCourseId(Long courseId) {
        log.debug("Fetching enrollments for course ID: {}", courseId);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    // ============== Template Method Steps ==============

    /**
     * Obtiene datos del estudiante desde ms-estudiantes.
     */
    private StudentDTO fetchStudentData(Long studentId) {
        log.debug("Fetching student data for ID: {}", studentId);
        return studentServiceClient.getStudentById(studentId);
    }

    /**
     * Obtiene datos del curso desde ms-cursos.
     */
    private CourseDTO fetchCourseData(Long courseId) {
        log.debug("Fetching course data for ID: {}", courseId);
        return courseServiceClient.getCourseById(courseId);
    }

    /**
     * Ejecuta todas las validaciones en orden usando Strategy Pattern.
     */
    private void executeValidations(EnrollmentCreateRequest request,
                                    StudentDTO student,
                                    CourseDTO course) {
        log.debug("Executing {} validation strategies", validationStrategies.size());

        validationStrategies.stream()
                .sorted(Comparator.comparingInt(EnrollmentValidationStrategy::getOrder))
                .forEach(strategy -> {
                    log.trace("Executing validation: {}", strategy.getClass().getSimpleName());
                    strategy.validate(request, student, course);
                });

        log.debug("All validations passed successfully");
    }

    /**
     * Crea y persiste la matrícula.
     */
    private Enrollment createAndSaveEnrollment(EnrollmentCreateRequest request) {
        Enrollment enrollment = enrollmentMapper.toEntity(request);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Post-procesamiento después de crear la matrícula (hook del Template Method).
     */
    private void postProcessEnrollment(Enrollment enrollment, StudentDTO student, CourseDTO course) {
        log.info("Enrollment completed: Student '{}' enrolled in course '{}' (Enrollment ID: {})",
                student.getFirstName() + " " + student.getLastName(),
                course.getName(),
                enrollment.getId());

        // Aquí se podrían agregar:
        // - Envío de notificaciones
        // - Publicación de eventos
        // - Actualización del contador de matriculados (si no se hace automáticamente)
    }

    /**
     * Busca una matrícula por ID o lanza excepción.
     */
    private Enrollment findEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Enrollment not found with ID: {}", id);
                    return ResourceNotFoundException.of("Enrollment", "id", id);
                });
    }
}

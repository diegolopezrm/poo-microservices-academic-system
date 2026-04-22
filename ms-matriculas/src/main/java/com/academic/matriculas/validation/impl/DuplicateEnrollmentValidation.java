package com.academic.matriculas.validation.impl;

import com.academic.common.exception.ConflictException;
import com.academic.matriculas.domain.dto.CourseDTO;
import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.StudentDTO;
import com.academic.matriculas.domain.enums.EnrollmentStatus;
import com.academic.matriculas.repository.EnrollmentRepository;
import com.academic.matriculas.validation.EnrollmentValidationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación: No debe existir matrícula duplicada.
 */
@Slf4j
@Component
@Order(6)
@RequiredArgsConstructor
public class DuplicateEnrollmentValidation implements EnrollmentValidationStrategy {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public void validate(EnrollmentCreateRequest request, StudentDTO student, CourseDTO course) {
        log.debug("Validating no duplicate enrollment for student {} in course {}",
                request.getStudentId(), request.getCourseId());

        boolean exists = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                request.getStudentId(),
                request.getCourseId(),
                EnrollmentStatus.ACTIVE
        );

        if (exists) {
            log.warn("Duplicate enrollment detected for student {} in course {}",
                    request.getStudentId(), request.getCourseId());
            throw ConflictException.enrollmentConflict(request.getStudentId(), request.getCourseId());
        }
    }

    @Override
    public int getOrder() {
        return 6;
    }
}

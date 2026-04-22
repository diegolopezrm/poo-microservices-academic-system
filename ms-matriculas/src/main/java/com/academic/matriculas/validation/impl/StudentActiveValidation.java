package com.academic.matriculas.validation.impl;

import com.academic.common.exception.BusinessException;
import com.academic.matriculas.domain.dto.CourseDTO;
import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.StudentDTO;
import com.academic.matriculas.validation.EnrollmentValidationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación: El estudiante debe estar activo.
 */
@Slf4j
@Component
@Order(2)
public class StudentActiveValidation implements EnrollmentValidationStrategy {

    @Override
    public void validate(EnrollmentCreateRequest request, StudentDTO student, CourseDTO course) {
        log.debug("Validating student {} is active", request.getStudentId());

        if (student != null && !student.isActive()) {
            log.warn("Student {} is not active", request.getStudentId());
            throw BusinessException.studentInactive(request.getStudentId());
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public boolean requiresStudent() {
        return true;
    }
}

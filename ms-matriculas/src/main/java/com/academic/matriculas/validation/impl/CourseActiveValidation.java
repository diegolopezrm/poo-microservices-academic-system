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
 * Estrategia de validación: El curso debe estar activo.
 */
@Slf4j
@Component
@Order(4)
public class CourseActiveValidation implements EnrollmentValidationStrategy {

    @Override
    public void validate(EnrollmentCreateRequest request, StudentDTO student, CourseDTO course) {
        log.debug("Validating course {} is active", request.getCourseId());

        if (course != null && !course.isActive()) {
            log.warn("Course {} is not active", request.getCourseId());
            throw BusinessException.courseInactive(request.getCourseId());
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public boolean requiresCourse() {
        return true;
    }
}

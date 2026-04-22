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
 * Estrategia de validación: El curso debe tener cupo disponible.
 */
@Slf4j
@Component
@Order(5)
public class CourseCapacityValidation implements EnrollmentValidationStrategy {

    @Override
    public void validate(EnrollmentCreateRequest request, StudentDTO student, CourseDTO course) {
        log.debug("Validating course {} has available capacity", request.getCourseId());

        if (course != null && !course.hasAvailableCapacity()) {
            log.warn("Course {} has no available capacity", request.getCourseId());
            throw BusinessException.courseFull(request.getCourseId());
        }
    }

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public boolean requiresCourse() {
        return true;
    }
}

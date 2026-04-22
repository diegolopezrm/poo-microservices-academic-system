package com.academic.cursos.domain.dto;

import com.academic.cursos.domain.enums.CourseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el estado de un curso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStatusRequest {

    @NotNull(message = "Status is required")
    private CourseStatus status;
}

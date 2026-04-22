package com.academic.cursos.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar la capacidad de un curso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCapacityRequest {

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 50, message = "Capacity must not exceed 50")
    private Integer capacity;
}

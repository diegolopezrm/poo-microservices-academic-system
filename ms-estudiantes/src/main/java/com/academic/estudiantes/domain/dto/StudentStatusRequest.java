package com.academic.estudiantes.domain.dto;

import com.academic.estudiantes.domain.enums.StudentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el estado de un estudiante.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentStatusRequest {

    @NotNull(message = "Status is required")
    private StudentStatus status;
}

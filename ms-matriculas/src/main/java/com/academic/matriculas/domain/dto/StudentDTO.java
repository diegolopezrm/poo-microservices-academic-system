package com.academic.matriculas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos de estudiante desde ms-estudiantes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    /**
     * Verifica si el estudiante está activo.
     *
     * @return true si el status es ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}

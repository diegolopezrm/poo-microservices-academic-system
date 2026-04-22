package com.academic.matriculas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos de curso desde ms-cursos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private Long id;
    private String name;
    private String description;
    private Integer capacity;
    private Integer enrolledCount;
    private Integer availableSpots;
    private String status;

    /**
     * Verifica si el curso está activo.
     *
     * @return true si el status es ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    /**
     * Verifica si hay cupo disponible.
     *
     * @return true si hay cupos disponibles
     */
    public boolean hasAvailableCapacity() {
        return availableSpots != null && availableSpots > 0;
    }
}

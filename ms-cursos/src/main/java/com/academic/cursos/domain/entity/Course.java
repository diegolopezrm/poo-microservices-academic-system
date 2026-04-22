package com.academic.cursos.domain.entity;

import com.academic.cursos.domain.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa un curso en el sistema académico.
 * Implementa el patrón de entidad rica con comportamiento encapsulado.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "enrolled_count", nullable = false)
    @Builder.Default
    private Integer enrolledCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CourseStatus status = CourseStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ============== Métodos de comportamiento (Entidad Rica) ==============

    /**
     * Activa el curso permitiendo matrículas.
     */
    public void activate() {
        this.status = CourseStatus.ACTIVE;
    }

    /**
     * Desactiva el curso impidiendo matrículas.
     */
    public void deactivate() {
        this.status = CourseStatus.INACTIVE;
    }

    /**
     * Verifica si el curso está activo.
     *
     * @return true si el curso está activo, false en caso contrario
     */
    public boolean isActive() {
        return this.status == CourseStatus.ACTIVE;
    }

    /**
     * Verifica si hay cupos disponibles en el curso.
     *
     * @return true si hay cupos disponibles, false en caso contrario
     */
    public boolean hasAvailableCapacity() {
        return this.enrolledCount < this.capacity;
    }

    /**
     * Obtiene el número de cupos disponibles.
     *
     * @return número de cupos disponibles
     */
    public int getAvailableSpots() {
        return Math.max(0, this.capacity - this.enrolledCount);
    }

    /**
     * Incrementa el contador de estudiantes matriculados.
     *
     * @throws IllegalStateException si no hay cupos disponibles
     */
    public void incrementEnrolled() {
        if (!hasAvailableCapacity()) {
            throw new IllegalStateException("No available capacity in course");
        }
        this.enrolledCount++;
    }

    /**
     * Decrementa el contador de estudiantes matriculados.
     */
    public void decrementEnrolled() {
        if (this.enrolledCount > 0) {
            this.enrolledCount--;
        }
    }
}

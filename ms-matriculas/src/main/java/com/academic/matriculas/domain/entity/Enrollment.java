package com.academic.matriculas.domain.entity;

import com.academic.matriculas.domain.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa una matrícula en el sistema académico.
 * Relaciona estudiantes con cursos.
 */
@Entity
@Table(name = "enrollments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id"}, name = "uk_enrollment_student_course")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @CreationTimestamp
    @Column(name = "enrollment_date", nullable = false, updatable = false)
    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    // ============== Métodos de comportamiento (Entidad Rica) ==============

    /**
     * Cancela la matrícula.
     */
    public void cancel() {
        this.status = EnrollmentStatus.CANCELLED;
    }

    /**
     * Verifica si la matrícula está activa.
     *
     * @return true si está activa, false en caso contrario
     */
    public boolean isActive() {
        return this.status == EnrollmentStatus.ACTIVE;
    }
}

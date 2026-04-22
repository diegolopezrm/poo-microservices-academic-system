package com.academic.estudiantes.domain.entity;

import com.academic.estudiantes.domain.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa un estudiante en el sistema académico.
 * Implementa el patrón de entidad rica con comportamiento encapsulado.
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ============== Métodos de comportamiento (Entidad Rica) ==============

    /**
     * Activa el estudiante permitiéndole matricularse en cursos.
     */
    public void activate() {
        this.status = StudentStatus.ACTIVE;
    }

    /**
     * Desactiva el estudiante impidiéndole matricularse en cursos.
     */
    public void deactivate() {
        this.status = StudentStatus.INACTIVE;
    }

    /**
     * Verifica si el estudiante está activo.
     *
     * @return true si el estudiante está activo, false en caso contrario
     */
    public boolean isActive() {
        return this.status == StudentStatus.ACTIVE;
    }

    /**
     * Obtiene el nombre completo del estudiante.
     *
     * @return nombre completo (nombre + apellido)
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}

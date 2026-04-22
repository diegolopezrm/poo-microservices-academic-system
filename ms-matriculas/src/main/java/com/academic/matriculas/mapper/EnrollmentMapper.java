package com.academic.matriculas.mapper;

import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.EnrollmentResponse;
import com.academic.matriculas.domain.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper para conversión entre entidades y DTOs de Enrollment.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnrollmentMapper {

    /**
     * Convierte un EnrollmentCreateRequest a entidad Enrollment.
     *
     * @param request DTO de creación
     * @return entidad Enrollment
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollmentDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Enrollment toEntity(EnrollmentCreateRequest request);

    /**
     * Convierte una entidad Enrollment a EnrollmentResponse.
     *
     * @param enrollment entidad Enrollment
     * @return DTO de respuesta
     */
    EnrollmentResponse toResponse(Enrollment enrollment);

    /**
     * Convierte una lista de entidades a lista de DTOs.
     *
     * @param enrollments lista de entidades
     * @return lista de DTOs
     */
    List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments);
}

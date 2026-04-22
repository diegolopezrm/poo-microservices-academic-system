package com.academic.estudiantes.mapper;

import com.academic.estudiantes.domain.dto.StudentCreateRequest;
import com.academic.estudiantes.domain.dto.StudentResponse;
import com.academic.estudiantes.domain.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper para conversión entre entidades y DTOs de Student.
 * Utiliza MapStruct para generación de código en tiempo de compilación.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    /**
     * Convierte un StudentCreateRequest a entidad Student.
     * El status se establece por defecto en la entidad (ACTIVE).
     *
     * @param request DTO de creación
     * @return entidad Student
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Student toEntity(StudentCreateRequest request);

    /**
     * Convierte una entidad Student a StudentResponse.
     *
     * @param student entidad Student
     * @return DTO de respuesta
     */
    StudentResponse toResponse(Student student);
}

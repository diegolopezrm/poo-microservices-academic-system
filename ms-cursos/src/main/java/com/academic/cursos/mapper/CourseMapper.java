package com.academic.cursos.mapper;

import com.academic.cursos.domain.dto.CourseCreateRequest;
import com.academic.cursos.domain.dto.CourseResponse;
import com.academic.cursos.domain.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper para conversión entre entidades y DTOs de Course.
 * Utiliza MapStruct para generación de código en tiempo de compilación.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    /**
     * Convierte un CourseCreateRequest a entidad Course.
     *
     * @param request DTO de creación
     * @return entidad Course
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrolledCount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Course toEntity(CourseCreateRequest request);

    /**
     * Convierte una entidad Course a CourseResponse.
     *
     * @param course entidad Course
     * @return DTO de respuesta
     */
    @Mapping(target = "availableSpots", expression = "java(course.getAvailableSpots())")
    CourseResponse toResponse(Course course);
}

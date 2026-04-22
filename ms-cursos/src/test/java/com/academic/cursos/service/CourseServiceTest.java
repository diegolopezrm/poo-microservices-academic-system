package com.academic.cursos.service;

import com.academic.common.exception.BusinessException;
import com.academic.common.exception.ResourceNotFoundException;
import com.academic.cursos.domain.dto.CourseCapacityRequest;
import com.academic.cursos.domain.dto.CourseCreateRequest;
import com.academic.cursos.domain.dto.CourseResponse;
import com.academic.cursos.domain.dto.CourseStatusRequest;
import com.academic.cursos.domain.entity.Course;
import com.academic.cursos.domain.enums.CourseStatus;
import com.academic.cursos.mapper.CourseMapper;
import com.academic.cursos.repository.CourseRepository;
import com.academic.cursos.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CourseService.
 * Valida la lógica de negocio de gestión de cursos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseCreateRequest createRequest;
    private Course course;
    private CourseResponse courseResponse;

    @BeforeEach
    void setUp() {
        createRequest = CourseCreateRequest.builder()
                .name("Programación Orientada a Objetos")
                .description("Curso de fundamentos de POO con Java")
                .capacity(30)
                .build();

        course = Course.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .description("Curso de fundamentos de POO con Java")
                .capacity(30)
                .enrolledCount(0)
                .status(CourseStatus.ACTIVE)
                .build();

        courseResponse = CourseResponse.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .description("Curso de fundamentos de POO con Java")
                .capacity(30)
                .enrolledCount(0)
                .availableSpots(30)
                .status(CourseStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe crear curso exitosamente")
    void shouldCreateCourseSuccessfully() {
        // Given
        when(courseMapper.toEntity(createRequest)).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        // When
        CourseResponse result = courseService.createCourse(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(createRequest.getName());
        assertThat(result.getCapacity()).isEqualTo(createRequest.getCapacity());
        assertThat(result.getAvailableSpots()).isEqualTo(30);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Debe obtener curso por ID exitosamente")
    void shouldGetCourseById_WhenCourseExists() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        // When
        CourseResponse result = courseService.getCourseById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(course.getName());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando curso no existe")
    void shouldThrowResourceNotFoundException_WhenCourseDoesNotExist() {
        // Given
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> courseService.getCourseById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course")
                .hasMessageContaining("999");

        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe actualizar capacidad cuando nueva capacidad es válida")
    void shouldUpdateCapacity_WhenNewCapacityIsValid() {
        // Given
        CourseCapacityRequest capacityRequest = CourseCapacityRequest.builder()
                .capacity(50)
                .build();
        Course updatedCourse = Course.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .description("Curso de fundamentos de POO con Java")
                .capacity(50)
                .enrolledCount(0)
                .status(CourseStatus.ACTIVE)
                .build();
        CourseResponse updatedResponse = CourseResponse.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .description("Curso de fundamentos de POO con Java")
                .capacity(50)
                .enrolledCount(0)
                .availableSpots(50)
                .status(CourseStatus.ACTIVE)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        when(courseMapper.toResponse(updatedCourse)).thenReturn(updatedResponse);

        // When
        CourseResponse result = courseService.updateCapacity(1L, capacityRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCapacity()).isEqualTo(50);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException cuando nueva capacidad es menor que inscritos")
    void shouldThrowBusinessException_WhenNewCapacityIsLessThanEnrolled() {
        // Given
        Course courseWithEnrollments = Course.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .capacity(30)
                .enrolledCount(20)
                .status(CourseStatus.ACTIVE)
                .build();
        CourseCapacityRequest capacityRequest = CourseCapacityRequest.builder()
                .capacity(10)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseWithEnrollments));

        // When & Then
        assertThatThrownBy(() -> courseService.updateCapacity(1L, capacityRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("capacity cannot be less than current enrolled count");

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Debe actualizar estado de curso exitosamente")
    void shouldUpdateCourseStatus_WhenCourseExists() {
        // Given
        CourseStatusRequest statusRequest = CourseStatusRequest.builder()
                .status(CourseStatus.INACTIVE)
                .build();
        Course updatedCourse = Course.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .capacity(30)
                .enrolledCount(0)
                .status(CourseStatus.INACTIVE)
                .build();
        CourseResponse updatedResponse = CourseResponse.builder()
                .id(1L)
                .name("Programación Orientada a Objetos")
                .description("Curso de fundamentos de POO con Java")
                .capacity(30)
                .enrolledCount(0)
                .availableSpots(30)
                .status(CourseStatus.INACTIVE)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        when(courseMapper.toResponse(updatedCourse)).thenReturn(updatedResponse);

        // When
        CourseResponse result = courseService.updateStatus(1L, statusRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(CourseStatus.INACTIVE);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
}

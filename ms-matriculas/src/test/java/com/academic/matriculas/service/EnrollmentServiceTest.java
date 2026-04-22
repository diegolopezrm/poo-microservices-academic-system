package com.academic.matriculas.service;

import com.academic.common.exception.BusinessException;
import com.academic.common.exception.ConflictException;
import com.academic.common.exception.ResourceNotFoundException;
import com.academic.matriculas.client.CourseServiceClient;
import com.academic.matriculas.client.StudentServiceClient;
import com.academic.matriculas.domain.dto.CourseDTO;
import com.academic.matriculas.domain.dto.EnrollmentCreateRequest;
import com.academic.matriculas.domain.dto.EnrollmentResponse;
import com.academic.matriculas.domain.dto.StudentDTO;
import com.academic.matriculas.domain.entity.Enrollment;
import com.academic.matriculas.domain.enums.EnrollmentStatus;
import com.academic.matriculas.mapper.EnrollmentMapper;
import com.academic.matriculas.repository.EnrollmentRepository;
import com.academic.matriculas.service.impl.EnrollmentServiceImpl;
import com.academic.matriculas.validation.EnrollmentValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para EnrollmentService.
 * Valida el patrón Strategy y Template Method en el proceso de matrícula.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("EnrollmentService Tests")
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @Mock
    private StudentServiceClient studentServiceClient;

    @Mock
    private CourseServiceClient courseServiceClient;

    @Mock
    private EnrollmentValidationStrategy studentActiveValidation;

    @Mock
    private EnrollmentValidationStrategy courseActiveValidation;

    @Mock
    private EnrollmentValidationStrategy courseCapacityValidation;

    @Mock
    private EnrollmentValidationStrategy duplicateEnrollmentValidation;

    private EnrollmentServiceImpl enrollmentService;

    private EnrollmentCreateRequest createRequest;
    private StudentDTO activeStudent;
    private StudentDTO inactiveStudent;
    private CourseDTO activeCourse;
    private CourseDTO fullCourse;
    private Enrollment enrollment;
    private EnrollmentResponse enrollmentResponse;

    @BeforeEach
    void setUp() {
        // Configurar validaciones con orden
        when(studentActiveValidation.getOrder()).thenReturn(1);
        when(courseActiveValidation.getOrder()).thenReturn(2);
        when(courseCapacityValidation.getOrder()).thenReturn(3);
        when(duplicateEnrollmentValidation.getOrder()).thenReturn(4);

        List<EnrollmentValidationStrategy> validations = List.of(
                studentActiveValidation,
                courseActiveValidation,
                courseCapacityValidation,
                duplicateEnrollmentValidation
        );

        // Constructor con orden correcto de @RequiredArgsConstructor
        enrollmentService = new EnrollmentServiceImpl(
                enrollmentRepository,
                enrollmentMapper,
                studentServiceClient,
                courseServiceClient,
                validations
        );

        createRequest = EnrollmentCreateRequest.builder()
                .studentId(1L)
                .courseId(1L)
                .build();

        activeStudent = StudentDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .status("ACTIVE")
                .build();

        inactiveStudent = StudentDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .status("INACTIVE")
                .build();

        activeCourse = CourseDTO.builder()
                .id(1L)
                .name("POO")
                .description("Programación OO")
                .capacity(30)
                .enrolledCount(10)
                .availableSpots(20)
                .status("ACTIVE")
                .build();

        fullCourse = CourseDTO.builder()
                .id(1L)
                .name("POO")
                .description("Programación OO")
                .capacity(30)
                .enrolledCount(30)
                .availableSpots(0)
                .status("ACTIVE")
                .build();

        enrollment = Enrollment.builder()
                .id(1L)
                .studentId(1L)
                .courseId(1L)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();

        enrollmentResponse = EnrollmentResponse.builder()
                .id(1L)
                .studentId(1L)
                .courseId(1L)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe crear matrícula exitosamente cuando todas las validaciones pasan")
    void shouldCreateEnrollmentSuccessfully_WhenAllValidationsPass() {
        // Given
        when(studentServiceClient.getStudentById(1L)).thenReturn(activeStudent);
        when(courseServiceClient.getCourseById(1L)).thenReturn(activeCourse);
        when(enrollmentMapper.toEntity(createRequest)).thenReturn(enrollment);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(enrollmentResponse);

        // When
        EnrollmentResponse result = enrollmentService.createEnrollment(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStudentId()).isEqualTo(1L);
        assertThat(result.getCourseId()).isEqualTo(1L);
        verify(studentServiceClient, times(1)).getStudentById(1L);
        verify(courseServiceClient, times(1)).getCourseById(1L);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando estudiante no existe")
    void shouldThrowResourceNotFoundException_WhenStudentDoesNotExist() {
        // Given
        when(studentServiceClient.getStudentById(999L))
                .thenThrow(ResourceNotFoundException.of("Student", "id", 999L));

        EnrollmentCreateRequest invalidRequest = EnrollmentCreateRequest.builder()
                .studentId(999L)
                .courseId(1L)
                .build();

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student");

        verify(studentServiceClient, times(1)).getStudentById(999L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException cuando estudiante está inactivo (Strategy Pattern)")
    void shouldThrowBusinessException_WhenStudentIsInactive() {
        // Given
        when(studentServiceClient.getStudentById(1L)).thenReturn(inactiveStudent);
        when(courseServiceClient.getCourseById(1L)).thenReturn(activeCourse);

        doThrow(BusinessException.studentInactive(1L))
                .when(studentActiveValidation).validate(any(), eq(inactiveStudent), eq(activeCourse));

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(createRequest))
                .isInstanceOf(BusinessException.class);

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Debe lanzar BusinessException cuando curso está lleno (Strategy Pattern)")
    void shouldThrowBusinessException_WhenCourseIsFull() {
        // Given
        when(studentServiceClient.getStudentById(1L)).thenReturn(activeStudent);
        when(courseServiceClient.getCourseById(1L)).thenReturn(fullCourse);

        doThrow(BusinessException.courseFull(1L))
                .when(courseCapacityValidation).validate(any(), eq(activeStudent), eq(fullCourse));

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(createRequest))
                .isInstanceOf(BusinessException.class);

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Debe lanzar ConflictException cuando ya existe matrícula duplicada (Strategy Pattern)")
    void shouldThrowConflictException_WhenDuplicateEnrollmentExists() {
        // Given
        when(studentServiceClient.getStudentById(1L)).thenReturn(activeStudent);
        when(courseServiceClient.getCourseById(1L)).thenReturn(activeCourse);

        doThrow(ConflictException.enrollmentConflict(1L, 1L))
                .when(duplicateEnrollmentValidation).validate(any(), eq(activeStudent), eq(activeCourse));

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(createRequest))
                .isInstanceOf(ConflictException.class);

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Debe obtener matrícula por ID exitosamente")
    void shouldGetEnrollmentById_WhenEnrollmentExists() {
        // Given
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(enrollmentResponse);

        // When
        EnrollmentResponse result = enrollmentService.getEnrollmentById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe validar que las estrategias se ejecutan en orden correcto (Patrón Strategy)")
    void shouldExecuteValidationsInCorrectOrder() {
        // Given
        when(studentServiceClient.getStudentById(1L)).thenReturn(activeStudent);
        when(courseServiceClient.getCourseById(1L)).thenReturn(activeCourse);
        when(enrollmentMapper.toEntity(createRequest)).thenReturn(enrollment);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(enrollmentResponse);

        // When
        enrollmentService.createEnrollment(createRequest);

        // Then - Verificar que las validaciones se llamaron en orden
        var inOrder = inOrder(studentActiveValidation, courseActiveValidation,
                             courseCapacityValidation, duplicateEnrollmentValidation);

        inOrder.verify(studentActiveValidation).validate(any(), any(), any());
        inOrder.verify(courseActiveValidation).validate(any(), any(), any());
        inOrder.verify(courseCapacityValidation).validate(any(), any(), any());
        inOrder.verify(duplicateEnrollmentValidation).validate(any(), any(), any());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando matrícula no existe")
    void shouldThrowResourceNotFoundException_WhenEnrollmentDoesNotExist() {
        // Given
        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.getEnrollmentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enrollment")
                .hasMessageContaining("999");

        verify(enrollmentRepository, times(1)).findById(999L);
    }
}

package com.academic.estudiantes.service;

import com.academic.common.exception.ConflictException;
import com.academic.common.exception.ResourceNotFoundException;
import com.academic.estudiantes.domain.dto.StudentCreateRequest;
import com.academic.estudiantes.domain.dto.StudentResponse;
import com.academic.estudiantes.domain.dto.StudentStatusRequest;
import com.academic.estudiantes.domain.entity.Student;
import com.academic.estudiantes.domain.enums.StudentStatus;
import com.academic.estudiantes.mapper.StudentMapper;
import com.academic.estudiantes.repository.StudentRepository;
import com.academic.estudiantes.service.impl.StudentServiceImpl;
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
 * Tests unitarios para StudentService.
 * Demuestra uso de JUnit 5 + Mockito para validar la lógica de negocio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentCreateRequest createRequest;
    private Student student;
    private StudentResponse studentResponse;

    @BeforeEach
    void setUp() {
        createRequest = StudentCreateRequest.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@university.edu")
                .build();

        student = Student.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@university.edu")
                .status(StudentStatus.ACTIVE)
                .build();

        studentResponse = StudentResponse.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@university.edu")
                .status(StudentStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe crear estudiante exitosamente cuando el email no existe")
    void shouldCreateStudentSuccessfully_WhenEmailDoesNotExist() {
        // Given
        when(studentRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(studentMapper.toEntity(createRequest)).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        // When
        StudentResponse result = studentService.createStudent(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(createRequest.getEmail());
        assertThat(result.getFirstName()).isEqualTo(createRequest.getFirstName());
        verify(studentRepository, times(1)).existsByEmail(createRequest.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe lanzar ConflictException cuando el email ya existe")
    void shouldThrowConflictException_WhenEmailAlreadyExists() {
        // Given
        when(studentRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> studentService.createStudent(createRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");

        verify(studentRepository, times(1)).existsByEmail(createRequest.getEmail());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe obtener estudiante por ID exitosamente")
    void shouldGetStudentById_WhenStudentExists() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        // When
        StudentResponse result = studentService.getStudentById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(student.getEmail());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando estudiante no existe")
    void shouldThrowResourceNotFoundException_WhenStudentDoesNotExist() {
        // Given
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> studentService.getStudentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student")
                .hasMessageContaining("999");

        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe actualizar estado de estudiante exitosamente")
    void shouldUpdateStudentStatus_WhenStudentExists() {
        // Given
        StudentStatusRequest statusRequest = StudentStatusRequest.builder()
                .status(StudentStatus.INACTIVE)
                .build();
        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@university.edu")
                .status(StudentStatus.INACTIVE)
                .build();
        StudentResponse updatedResponse = StudentResponse.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@university.edu")
                .status(StudentStatus.INACTIVE)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);
        when(studentMapper.toResponse(updatedStudent)).thenReturn(updatedResponse);

        // When
        StudentResponse result = studentService.updateStatus(1L, statusRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StudentStatus.INACTIVE);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }
}

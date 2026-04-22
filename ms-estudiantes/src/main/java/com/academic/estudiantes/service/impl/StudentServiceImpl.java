package com.academic.estudiantes.service.impl;

import com.academic.common.exception.ConflictException;
import com.academic.common.exception.ResourceNotFoundException;
import com.academic.estudiantes.domain.dto.StudentCreateRequest;
import com.academic.estudiantes.domain.dto.StudentResponse;
import com.academic.estudiantes.domain.dto.StudentStatusRequest;
import com.academic.estudiantes.domain.entity.Student;
import com.academic.estudiantes.domain.enums.StudentStatus;
import com.academic.estudiantes.mapper.StudentMapper;
import com.academic.estudiantes.repository.StudentRepository;
import com.academic.estudiantes.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de estudiantes.
 * Contiene la lógica de negocio siguiendo el principio SRP.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentResponse> getAllStudents() {
        log.debug("Fetching all students");
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        log.info("Creating student with email: {}", request.getEmail());

        // Verificar que el email no exista
        if (studentRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw ConflictException.emailAlreadyExists(request.getEmail());
        }

        // Crear y persistir el estudiante
        Student student = studentMapper.toEntity(request);
        student.setStatus(StudentStatus.ACTIVE);

        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", savedStudent.getId());

        return studentMapper.toResponse(savedStudent);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        log.debug("Fetching student with ID: {}", id);

        Student student = findStudentById(id);
        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse updateStatus(Long id, StudentStatusRequest request) {
        log.info("Updating status of student {} to {}", id, request.getStatus());

        Student student = findStudentById(id);

        // Actualizar estado usando métodos de la entidad rica
        if (request.getStatus() == StudentStatus.ACTIVE) {
            student.activate();
        } else {
            student.deactivate();
        }

        Student updatedStudent = studentRepository.save(student);
        log.info("Student {} status updated to {}", id, updatedStudent.getStatus());

        return studentMapper.toResponse(updatedStudent);
    }

    /**
     * Método privado para buscar estudiante por ID o lanzar excepción.
     *
     * @param id ID del estudiante
     * @return entidad Student
     * @throws ResourceNotFoundException si el estudiante no existe
     */
    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found with ID: {}", id);
                    return ResourceNotFoundException.of("Student", "id", id);
                });
    }
}

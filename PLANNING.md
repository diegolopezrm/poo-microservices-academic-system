# 📋 Plan de Proyecto: Sistema Académico con Microservicios

## Información del Proyecto

| Campo               | Valor                        |
| ------------------- | ---------------------------- |
| **Nombre**          | Sistema de Gestión Académica |
| **Tipo**            | Actividad de Homologación    |
| **Stack Principal** | Java 21 + Spring Boot 3.x    |
| **Arquitectura**    | Microservicios               |

---

## 🎯 Objetivo General

Demostrar dominio de **Programación Orientada a Objetos (POO)**, principios **SOLID**, **patrones de diseño** y **arquitectura distribuida** mediante la implementación de un sistema académico basado en microservicios.

---

## 📐 Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENTES                                │
│                    (Postman / Frontend)                         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     API GATEWAY (:8080)                         │
│              Enrutamiento, Logging, Correlation-Id              │
└─────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌───────────────┐     ┌───────────────┐     ┌───────────────┐
│ MS-ESTUDIANTES│     │   MS-CURSOS   │     │ MS-MATRICULAS │
│    (:8081)    │     │    (:8082)    │     │    (:8083)    │
│               │     │               │     │               │
│  ┌─────────┐  │     │  ┌─────────┐  │     │  ┌─────────┐  │
│  │   H2    │  │     │  │   H2    │  │     │  │   H2    │  │
│  │   DB    │  │     │  │   DB    │  │     │  │   DB    │  │
│  └─────────┘  │     │  └─────────┘  │     │  └─────────┘  │
└───────────────┘     └───────────────┘     └───────────────┘
```

### Comunicación entre Servicios

```
┌─────────────────┐         ┌───────────────┐
│  MS-MATRICULAS  │ ──GET─▶ │ MS-ESTUDIANTES│
│                 │         └───────────────┘
│                 │         ┌───────────────┐
│                 │ ──GET─▶ │   MS-CURSOS   │
└─────────────────┘         └───────────────┘
```

---

## 📁 Estructura del Repositorio

```
poo-project/
├── 📄 PLANNING.md                    # Este documento
├── 📄 README.md                      # Instrucciones de ejecución
├── 📂 docs/
│   ├── 📄 DESIGN.md                  # Documento de diseño técnico
│   ├── 📂 diagrams/
│   │   ├── 📄 class-diagram.puml     # Diagrama de clases
│   │   ├── 📄 sequence-diagram.puml  # Diagrama de secuencia
│   │   └── 📄 architecture.puml      # Diagrama de arquitectura
│   └── 📂 postman/
│       └── 📄 academic-system.json   # Colección Postman
├── 📂 ms-estudiantes/                # Microservicio de estudiantes
├── 📂 ms-cursos/                     # Microservicio de cursos
├── 📂 ms-matriculas/                 # Microservicio de matrículas
├── 📂 api-gateway/                   # API Gateway
└── 📄 docker-compose.yml             # Orquestación (opcional)
```

---

## 🧱 Estructura Interna de cada Microservicio

```
ms-{nombre}/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/academic/{nombre}/
    │   │   ├── {Nombre}Application.java
    │   │   ├── 📂 config/
    │   │   │   ├── WebConfig.java
    │   │   │   └── RestClientConfig.java
    │   │   ├── 📂 controller/
    │   │   │   └── {Entity}Controller.java
    │   │   ├── 📂 service/
    │   │   │   ├── {Entity}Service.java
    │   │   │   └── impl/
    │   │   │       └── {Entity}ServiceImpl.java
    │   │   ├── 📂 repository/
    │   │   │   └── {Entity}Repository.java
    │   │   ├── 📂 domain/
    │   │   │   ├── 📂 entity/
    │   │   │   │   └── {Entity}.java
    │   │   │   ├── 📂 dto/
    │   │   │   │   ├── {Entity}RequestDTO.java
    │   │   │   │   └── {Entity}ResponseDTO.java
    │   │   │   └── 📂 enums/
    │   │   │       └── Status.java
    │   │   ├── 📂 mapper/
    │   │   │   └── {Entity}Mapper.java
    │   │   ├── 📂 exception/
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   ├── BusinessException.java
    │   │   │   └── ResourceNotFoundException.java
    │   │   ├── 📂 validation/                    # Patrones de diseño
    │   │   │   ├── ValidationStrategy.java
    │   │   │   └── impl/
    │   │   └── 📂 filter/
    │   │       └── CorrelationIdFilter.java
    │   └── resources/
    │       ├── application.yml
    │       └── application-dev.yml
    └── test/
        ├── java/com/academic/{nombre}/
        │   ├── 📂 unit/
        │   │   ├── service/
        │   │   └── controller/
        │   └── 📂 integration/
        │       └── {Entity}IntegrationTest.java
        └── resources/
            └── application-test.yml
```

---

## 🔧 Stack Tecnológico Detallado

| Categoría         | Tecnología           | Versión | Propósito               |
| ----------------- | -------------------- | ------- | ----------------------- |
| **Lenguaje**      | Java                 | 21 LTS  | Lenguaje principal      |
| **Framework**     | Spring Boot          | 3.2.x   | Framework base          |
| **Build Tool**    | Maven                | 3.9.x   | Gestión de dependencias |
| **Web**           | Spring Web           | -       | REST APIs               |
| **Persistencia**  | Spring Data JPA      | -       | Acceso a datos          |
| **Base de Datos** | H2 Database          | -       | BD embebida (dev)       |
| **Validación**    | Jakarta Validation   | -       | Validaciones            |
| **Monitoreo**     | Spring Actuator      | -       | Health checks           |
| **Testing**       | JUnit 5 + Mockito    | -       | Pruebas                 |
| **Documentación** | OpenAPI/Swagger      | -       | Documentación API       |
| **Gateway**       | Spring Cloud Gateway | -       | API Gateway             |

---

## 📊 Modelo de Datos

### Entidad: Student (ms-estudiantes)

| Campo     | Tipo          | Restricciones             |
| --------- | ------------- | ------------------------- |
| id        | Long          | PK, Auto-generated        |
| firstName | String        | @NotBlank, max 50         |
| lastName  | String        | @NotBlank, max 50         |
| email     | String        | @Email, @NotBlank, unique |
| status    | StudentStatus | ACTIVE, INACTIVE          |
| createdAt | LocalDateTime | Auto-generated            |
| updatedAt | LocalDateTime | Auto-updated              |

### Entidad: Course (ms-cursos)

| Campo         | Tipo          | Restricciones      |
| ------------- | ------------- | ------------------ |
| id            | Long          | PK, Auto-generated |
| name          | String        | @NotBlank, max 100 |
| description   | String        | max 500            |
| capacity      | Integer       | @Min(1), @Max(50)  |
| enrolledCount | Integer       | Default 0          |
| status        | CourseStatus  | ACTIVE, INACTIVE   |
| createdAt     | LocalDateTime | Auto-generated     |
| updatedAt     | LocalDateTime | Auto-updated       |

### Entidad: Enrollment (ms-matriculas)

| Campo          | Tipo             | Restricciones      |
| -------------- | ---------------- | ------------------ |
| id             | Long             | PK, Auto-generated |
| studentId      | Long             | @NotNull           |
| courseId       | Long             | @NotNull           |
| enrollmentDate | LocalDateTime    | Auto-generated     |
| status         | EnrollmentStatus | ACTIVE, CANCELLED  |

---

## 🎨 Patrones de Diseño a Implementar

### 1. Strategy Pattern (Validaciones de Matrícula)

```java
// Interface
public interface EnrollmentValidationStrategy {
    void validate(EnrollmentRequest request) throws BusinessException;
}

// Implementaciones
@Component
public class StudentExistsValidation implements EnrollmentValidationStrategy { }

@Component
public class StudentActiveValidation implements EnrollmentValidationStrategy { }

@Component
public class CourseExistsValidation implements EnrollmentValidationStrategy { }

@Component
public class CourseActiveValidation implements EnrollmentValidationStrategy { }

@Component
public class CourseCapacityValidation implements EnrollmentValidationStrategy { }

@Component
public class DuplicateEnrollmentValidation implements EnrollmentValidationStrategy { }
```

### 2. Template Method Pattern (Pipeline de Matrícula)

```java
public abstract class EnrollmentTemplate {

    public final Enrollment processEnrollment(EnrollmentRequest request) {
        validateRequest(request);          // Step 1
        validateBusinessRules(request);    // Step 2 - Abstract
        Enrollment enrollment = createEnrollment(request);  // Step 3
        postProcessing(enrollment);        // Step 4 - Hook
        return enrollment;
    }

    protected abstract void validateBusinessRules(EnrollmentRequest request);
    protected void postProcessing(Enrollment enrollment) { } // Hook opcional
}
```

### 3. Repository Pattern (Spring Data JPA)

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    List<Student> findByStatus(StudentStatus status);
    boolean existsByEmail(String email);
}
```

---

## ⚠️ Manejo de Errores Estandarizado

### Estructura de Error Response

```java
public record ErrorResponse(
    LocalDateTime timestamp,
    String path,
    String errorCode,
    String message,
    List<String> details
) { }
```

### Códigos de Error por Servicio

| Código                 | HTTP Status | Descripción                   |
| ---------------------- | ----------- | ----------------------------- |
| `VALIDATION_ERROR`     | 400         | Error de validación de campos |
| `STUDENT_NOT_FOUND`    | 404         | Estudiante no encontrado      |
| `COURSE_NOT_FOUND`     | 404         | Curso no encontrado           |
| `ENROLLMENT_NOT_FOUND` | 404         | Matrícula no encontrada       |
| `ENROLLMENT_CONFLICT`  | 409         | Matrícula duplicada           |
| `STUDENT_INACTIVE`     | 422         | Estudiante inactivo           |
| `COURSE_INACTIVE`      | 422         | Curso inactivo                |
| `COURSE_FULL`          | 422         | Curso sin cupo disponible     |
| `INTERNAL_ERROR`       | 500         | Error interno del servidor    |

---

## 🔌 API Endpoints

### MS-Estudiantes (Puerto 8081)

| Método | Endpoint                       | Descripción               |
| ------ | ------------------------------ | ------------------------- |
| POST   | `/api/v1/students`             | Crear estudiante          |
| GET    | `/api/v1/students/{id}`        | Obtener estudiante por ID |
| PATCH  | `/api/v1/students/{id}/status` | Actualizar estado         |

### MS-Cursos (Puerto 8082)

| Método | Endpoint                        | Descripción          |
| ------ | ------------------------------- | -------------------- |
| POST   | `/api/v1/courses`               | Crear curso          |
| GET    | `/api/v1/courses/{id}`          | Obtener curso por ID |
| PATCH  | `/api/v1/courses/{id}/status`   | Actualizar estado    |
| PATCH  | `/api/v1/courses/{id}/capacity` | Actualizar capacidad |

### MS-Matrículas (Puerto 8083)

| Método | Endpoint                             | Descripción              |
| ------ | ------------------------------------ | ------------------------ |
| POST   | `/api/v1/enrollments`                | Crear matrícula          |
| GET    | `/api/v1/enrollments/{id}`           | Obtener matrícula por ID |
| GET    | `/api/v1/enrollments?studentId={id}` | Buscar por estudiante    |
| GET    | `/api/v1/enrollments?courseId={id}`  | Buscar por curso         |

---

## ✅ Plan de Testing

### Pruebas Unitarias (Mínimo 8)

| #   | Servicio       | Clase                 | Test                                  |
| --- | -------------- | --------------------- | ------------------------------------- |
| 1   | ms-estudiantes | StudentServiceTest    | `shouldCreateStudentSuccessfully`     |
| 2   | ms-estudiantes | StudentServiceTest    | `shouldThrowExceptionWhenEmailExists` |
| 3   | ms-cursos      | CourseServiceTest     | `shouldCreateCourseSuccessfully`      |
| 4   | ms-cursos      | CourseServiceTest     | `shouldUpdateCapacitySuccessfully`    |
| 5   | ms-matriculas  | EnrollmentServiceTest | `shouldCreateEnrollmentSuccessfully`  |
| 6   | ms-matriculas  | EnrollmentServiceTest | `shouldThrowWhenStudentNotFound`      |
| 7   | ms-matriculas  | EnrollmentServiceTest | `shouldThrowWhenCourseIsFull`         |
| 8   | ms-matriculas  | EnrollmentServiceTest | `shouldThrowWhenDuplicateEnrollment`  |

### Pruebas de Integración (Mínimo 2 por servicio)

| #   | Servicio       | Test                                         |
| --- | -------------- | -------------------------------------------- |
| 1   | ms-estudiantes | `shouldCreateAndRetrieveStudent`             |
| 2   | ms-estudiantes | `shouldUpdateStudentStatus`                  |
| 3   | ms-cursos      | `shouldCreateAndRetrieveCourse`              |
| 4   | ms-cursos      | `shouldUpdateCourseCapacity`                 |
| 5   | ms-matriculas  | `shouldCreateEnrollmentWithValidData`        |
| 6   | ms-matriculas  | `shouldReturnConflictForDuplicateEnrollment` |

---

## 📦 Fases de Desarrollo

### Fase 1: Configuración Base (Día 1)

- [ ] Crear estructura de proyecto Maven multi-módulo
- [ ] Configurar dependencias comunes
- [ ] Crear módulo `common` con DTOs compartidos
- [ ] Configurar propiedades de cada microservicio
- [ ] Configurar Actuator para health checks

### Fase 2: MS-Estudiantes (Días 2-3)

- [ ] Crear entidad Student
- [ ] Crear DTOs (Request/Response)
- [ ] Implementar Repository
- [ ] Implementar Service con validaciones
- [ ] Implementar Controller
- [ ] Implementar manejo de errores
- [ ] Implementar filtro Correlation-Id
- [ ] Escribir pruebas unitarias
- [ ] Escribir pruebas de integración

### Fase 3: MS-Cursos (Días 4-5)

- [ ] Crear entidad Course
- [ ] Crear DTOs (Request/Response)
- [ ] Implementar Repository
- [ ] Implementar Service con validaciones
- [ ] Implementar Controller
- [ ] Implementar manejo de errores
- [ ] Escribir pruebas unitarias
- [ ] Escribir pruebas de integración

### Fase 4: MS-Matrículas (Días 6-8)

- [ ] Crear entidad Enrollment
- [ ] Crear DTOs (Request/Response)
- [ ] Implementar Repository
- [ ] Implementar RestClient para comunicación
- [ ] **Implementar Strategy Pattern** para validaciones
- [ ] **Implementar Template Method** para pipeline
- [ ] Implementar Service con orquestación
- [ ] Implementar Controller
- [ ] Implementar manejo de errores
- [ ] Escribir pruebas unitarias
- [ ] Escribir pruebas de integración

### Fase 5: API Gateway (Día 9)

- [ ] Configurar Spring Cloud Gateway
- [ ] Configurar rutas hacia servicios
- [ ] Implementar filtro global Correlation-Id
- [ ] Configurar logging centralizado
- [ ] Configurar CORS

### Fase 6: Documentación y Entregables (Días 10-11)

- [ ] Generar diagramas UML (PlantUML)
- [ ] Crear documento de diseño PDF
- [ ] Crear colección Postman con 10+ requests
- [ ] Escribir README.md completo
- [ ] Grabar video demostración (5-8 min)

### Fase 7: Revisión y Refinamiento (Día 12)

- [ ] Code review y refactoring
- [ ] Verificar cobertura de tests
- [ ] Verificar cumplimiento de SOLID
- [ ] Preparar sustentación técnica

---

## 📝 Checklist de Principios SOLID

### Single Responsibility Principle (SRP)

- [ ] Cada clase tiene una única responsabilidad
- [ ] Controllers solo manejan HTTP
- [ ] Services contienen lógica de negocio
- [ ] Repositories solo acceso a datos

### Open/Closed Principle (OCP)

- [ ] Strategy Pattern permite agregar validaciones sin modificar código existente
- [ ] Uso de interfaces para extensibilidad

### Liskov Substitution Principle (LSP)

- [ ] Las subclases pueden sustituir a sus clases base
- [ ] Implementaciones de estrategias intercambiables

### Interface Segregation Principle (ISP)

- [ ] Interfaces pequeñas y específicas
- [ ] Clientes no dependen de métodos que no usan

### Dependency Inversion Principle (DIP)

- [ ] Inyección de dependencias con Spring
- [ ] Dependencia de abstracciones, no implementaciones

---

## 📊 Métricas de Calidad

| Métrica                     | Objetivo        |
| --------------------------- | --------------- |
| Cobertura de código (tests) | > 70%           |
| Complejidad ciclomática     | < 10 por método |
| Duplicación de código       | < 3%            |
| Health checks funcionando   | 4/4 servicios   |

---

## 🚀 Comandos de Ejecución

```bash
# Compilar todo el proyecto
mvn clean install

# Ejecutar cada microservicio (en terminales separadas)
cd ms-estudiantes && mvn spring-boot:run
cd ms-cursos && mvn spring-boot:run
cd ms-matriculas && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run

# Ejecutar tests
mvn test

# Verificar health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8080/actuator/health
```

---

## 📚 Referencias y Recursos

- [Spring Boot 3 Documentation](https://docs.spring.io/spring-boot/docs/3.2.x/reference/html/)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Design Patterns - Refactoring Guru](https://refactoring.guru/design-patterns)
- [SOLID Principles](https://www.baeldung.com/solid-principles)

---

## 📋 Entregables Finales

| Entregable               | Formato       | Estado       |
| ------------------------ | ------------- | ------------ |
| Repositorio GitHub       | Git           | ⏳ Pendiente |
| Documento de Diseño      | PDF           | ⏳ Pendiente |
| Diagrama de Clases       | PNG/SVG       | ⏳ Pendiente |
| Diagrama de Secuencia    | PNG/SVG       | ⏳ Pendiente |
| Diagrama de Arquitectura | PNG/SVG       | ⏳ Pendiente |
| Colección Postman        | JSON          | ⏳ Pendiente |
| Video Demostración       | MP4 (5-8 min) | ⏳ Pendiente |

---

## 📅 Estimación de Tiempo

| Fase               | Duración Estimada |
| ------------------ | ----------------- |
| Configuración Base | 4-6 horas         |
| MS-Estudiantes     | 6-8 horas         |
| MS-Cursos          | 6-8 horas         |
| MS-Matrículas      | 10-12 horas       |
| API Gateway        | 3-4 horas         |
| Documentación      | 4-6 horas         |
| Testing adicional  | 4-6 horas         |
| Video y revisión   | 3-4 horas         |
| **TOTAL**          | **40-54 horas**   |

---

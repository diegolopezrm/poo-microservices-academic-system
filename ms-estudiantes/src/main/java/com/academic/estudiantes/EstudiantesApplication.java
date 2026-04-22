package com.academic.estudiantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del microservicio de Estudiantes.
 * Puerto: 8081
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.academic.estudiantes", "com.academic.common"})
public class EstudiantesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstudiantesApplication.class, args);
    }
}

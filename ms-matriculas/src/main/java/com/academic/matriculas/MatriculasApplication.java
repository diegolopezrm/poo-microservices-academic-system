package com.academic.matriculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del microservicio de Matrículas.
 * Puerto: 8083
 *
 * Este microservicio orquesta la comunicación con ms-estudiantes y ms-cursos
 * para validar y procesar las matrículas.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.academic.matriculas", "com.academic.common"})
public class MatriculasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatriculasApplication.class, args);
    }
}

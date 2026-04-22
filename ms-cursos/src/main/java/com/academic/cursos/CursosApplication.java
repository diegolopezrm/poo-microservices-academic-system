package com.academic.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del microservicio de Cursos.
 * Puerto: 8082
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.academic.cursos", "com.academic.common"})
public class CursosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CursosApplication.class, args);
    }
}

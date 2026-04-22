package com.academic.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del API Gateway.
 * Puerto: 8080
 *
 * Funcionalidades:
 * - Enrutamiento hacia microservicios
 * - Generación de Correlation ID para trazabilidad
 * - Logging centralizado
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

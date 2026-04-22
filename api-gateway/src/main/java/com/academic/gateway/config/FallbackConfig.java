package com.academic.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Configuración de rutas de fallback cuando los servicios no están disponibles.
 */
@Configuration
public class FallbackConfig {

    @Bean
    public RouterFunction<ServerResponse> fallbackRoutes() {
        return RouterFunctions.route()
                .GET("/fallback/students", request ->
                    createFallbackResponse("ms-estudiantes"))
                .POST("/fallback/students", request ->
                    createFallbackResponse("ms-estudiantes"))
                .GET("/fallback/courses", request ->
                    createFallbackResponse("ms-cursos"))
                .POST("/fallback/courses", request ->
                    createFallbackResponse("ms-cursos"))
                .GET("/fallback/enrollments", request ->
                    createFallbackResponse("ms-matriculas"))
                .POST("/fallback/enrollments", request ->
                    createFallbackResponse("ms-matriculas"))
                .build();
    }

    private reactor.core.publisher.Mono<ServerResponse> createFallbackResponse(String serviceName) {
        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "path", "/fallback/" + serviceName,
                "errorCode", "SERVICE_UNAVAILABLE",
                "message", "Service " + serviceName + " is currently unavailable. Please try again later.",
                "details", java.util.List.of("service=" + serviceName)
        );

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }
}

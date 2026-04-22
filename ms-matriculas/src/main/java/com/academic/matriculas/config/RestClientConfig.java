package com.academic.matriculas.config;

import com.academic.common.filter.CorrelationIdFilter;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

/**
 * Configuración de los clientes REST para comunicación con otros microservicios.
 * Propaga el Correlation ID entre servicios.
 */
@Configuration
public class RestClientConfig {

    @Value("${services.students.url:http://localhost:8081}")
    private String studentsServiceUrl;

    @Value("${services.courses.url:http://localhost:8082}")
    private String coursesServiceUrl;

    /**
     * RestClient configurado para comunicarse con ms-estudiantes.
     */
    @Bean
    public RestClient studentRestClient() {
        return RestClient.builder()
                .baseUrl(studentsServiceUrl + "/api/v1")
                .requestInterceptor(correlationIdInterceptor())
                .build();
    }

    /**
     * RestClient configurado para comunicarse con ms-cursos.
     */
    @Bean
    public RestClient courseRestClient() {
        return RestClient.builder()
                .baseUrl(coursesServiceUrl + "/api/v1")
                .requestInterceptor(correlationIdInterceptor())
                .build();
    }

    /**
     * Interceptor que propaga el Correlation ID a las llamadas entre servicios.
     */
    private ClientHttpRequestInterceptor correlationIdInterceptor() {
        return (request, body, execution) -> {
            String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
            if (correlationId != null) {
                request.getHeaders().add(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId);
            }
            return execution.execute(request, body);
        };
    }
}

package com.academic.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Filtro global que asegura la presencia del Correlation ID en todas las peticiones.
 * Si no existe, genera uno nuevo y lo propaga hacia los microservicios.
 */
@Component
public class CorrelationIdGatewayFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdGatewayFilter.class);

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = generateCorrelationId();
            log.debug("Generated new Correlation ID: {}", correlationId);
        } else {
            log.debug("Using existing Correlation ID: {}", correlationId);
        }

        final String finalCorrelationId = correlationId;

        // Agregar el correlation ID al request que va hacia los microservicios
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(CORRELATION_ID_HEADER, correlationId)
                .build();

        // Agregar el correlation ID al response que va hacia el cliente
        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, correlationId);

        log.info("[{}] {} {} -> Routing request",
                finalCorrelationId,
                request.getMethod(),
                request.getURI().getPath());

        return chain.filter(exchange.mutate().request(modifiedRequest).build())
                .then(Mono.fromRunnable(() -> {
                    log.info("[{}] {} {} -> Response status: {}",
                            finalCorrelationId,
                            request.getMethod(),
                            request.getURI().getPath(),
                            exchange.getResponse().getStatusCode());
                }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}

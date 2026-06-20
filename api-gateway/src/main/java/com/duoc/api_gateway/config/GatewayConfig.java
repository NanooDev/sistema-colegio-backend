package com.duoc.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class GatewayConfig {

    @Value("${SERVICIO_ESTUDIANTES_URL:http://localhost:8081}")
    private String estudiantesUrl;

    @Value("${SERVICIO_PROFESORES_URL:http://localhost:8082}")
    private String profesoresUrl;

    @Value("${SERVICIO_ASISTENCIAS_URL:http://localhost:8083}")
    private String asistenciasUrl;

    @Value("${SERVICIO_CURSOS_URL:http://localhost:8084}")
    private String cursosUrl;

    @Value("${SERVICIO_ASIGNATURAS_URL:http://localhost:8085}")
    private String asignaturasUrl;

    @Value("${SERVICIO_MATRICULAS_URL:http://localhost:8086}")
    private String matriculasUrl;

    @Value("${SERVICIO_BIBLIOTECA_URL:http://localhost:8087}")
    private String bibliotecaUrl;

    @Value("${SERVICIO_FINANZAS_URL:http://localhost:8088}")
    private String finanzasUrl;

    @Value("${SERVICIO_NOTIFICACIONES_URL:http://localhost:8089}")
    private String notificacionesUrl;

    @Value("${SERVICIO_CALIFICACIONES_URL:http://localhost:8095}")
    private String calificacionesUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("estudiantes", r -> r.path("/gateway/estudiantes/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(estudiantesUrl))
            .route("profesores", r -> r.path("/gateway/profesores/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(profesoresUrl))
            .route("asistencias", r -> r.path("/gateway/asistencias/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(asistenciasUrl))
            .route("cursos", r -> r.path("/gateway/cursos/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(cursosUrl))
            .route("asignaturas", r -> r.path("/gateway/asignaturas/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(asignaturasUrl))
            .route("matriculas", r -> r.path("/gateway/matriculas/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(matriculasUrl))
            .route("biblioteca", r -> r.path("/gateway/biblioteca/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(bibliotecaUrl))
            .route("finanzas", r -> r.path("/gateway/finanzas/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(finanzasUrl))
            .route("notificaciones", r -> r.path("/gateway/notificaciones/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(notificacionesUrl))
            .route("calificaciones", r -> r.path("/gateway/calificaciones/**")
                .filters(f -> f.stripPrefix(2).preserveHostHeader())
                .uri(calificacionesUrl))
            .build();
    }
}

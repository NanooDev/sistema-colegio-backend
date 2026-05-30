package com.duoc.servicio_cursos.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Servicio de Cursos")
                        .version("1.0")
                        .description("Documentación de la API para el servicio de gestión de cursos"));
    }
}

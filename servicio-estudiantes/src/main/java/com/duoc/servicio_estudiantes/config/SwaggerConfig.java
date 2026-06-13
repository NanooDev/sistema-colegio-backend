package com.duoc.servicio_estudiantes.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Servicio Estudiantes")
                        .description("API para el microservicio de Estudiantes")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("DUOC UC - Fullstack I")
                                .url("https://www.duoc.cl")));
    }
}

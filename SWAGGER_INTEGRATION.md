# Integración de Swagger en el Sistema de Colegio

## Descripción General

Se ha integrado Swagger (OpenAPI 3.0) en todos los microservicios del sistema de colegio para facilitar la documentación interactiva de las APIs REST.

## Cambios Realizados

### 1. **Dependencias Maven (pom.xml)**

Se agregó la siguiente dependencia a todos los microservicios:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### 2. **Configuración de Swagger (SwaggerConfig.java)**

Se creó un archivo de configuración `SwaggerConfig.java` en cada microservicio con la siguiente estructura:

- Ubicación: `src/main/java/com/duoc/[servicio]/config/SwaggerConfig.java`
- Cada configuración personaliza el título, versión y descripción de la API

**Ejemplo de configuración:**
```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Servicio de [Nombre]")
                        .version("1.0")
                        .description("Documentación de la API para el servicio de gestión de [Dominio]"));
    }
}
```

### 3. **Propiedades de la Aplicación (application.properties)**

Se agregaron las siguientes propiedades a cada `application.properties`:

```properties
# Swagger Configuration
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

## Microservicios Configurados

| Servicio | Puerto | Swagger UI | OpenAPI Spec |
|----------|--------|-----------|--------------|
| Asignaturas | 8085 | http://localhost:8085/doc/swagger-ui.html | http://localhost:8085/v3/api-docs |
| Asistencias | 8083 | http://localhost:8083/doc/swagger-ui.html | http://localhost:8083/v3/api-docs |
| Biblioteca | 8087 | http://localhost:8087/doc/swagger-ui.html | http://localhost:8087/v3/api-docs |
| Calificaciones | 8095 | http://localhost:8095/doc/swagger-ui.html | http://localhost:8095/v3/api-docs |
| Cursos | 8084 | http://localhost:8084/doc/swagger-ui.html | http://localhost:8084/v3/api-docs |
| Estudiantes | 8081 | http://localhost:8081/doc/swagger-ui.html | http://localhost:8081/v3/api-docs |
| Finanzas | 8088 | http://localhost:8088/doc/swagger-ui.html | http://localhost:8088/v3/api-docs |
| Matrículas | 8086 | http://localhost:8086/doc/swagger-ui.html | http://localhost:8086/v3/api-docs |
| Notificaciones | 8089 | http://localhost:8089/doc/swagger-ui.html | http://localhost:8089/v3/api-docs |
| Profesores | 8082 | http://localhost:8082/doc/swagger-ui.html | http://localhost:8082/v3/api-docs |

## Cómo Acceder a la Documentación

### 1. **Iniciar los Microservicios**

Cada microservicio debe compilarse y ejecutarse:

```bash
cd servicio-[nombre]
./mvnw clean install
./mvnw spring-boot:run
```

### 2. **Acceder a Swagger UI**

Una vez que el servicio esté ejecutándose, accede a la interfaz interactiva:

- Reemplaza `[puerto]` con el puerto del servicio
- URL: `http://localhost:[puerto]/doc/swagger-ui.html`

**Ejemplo para Estudiantes:**
```
http://localhost:8081/doc/swagger-ui.html
```

### 3. **Acceder al Spec de OpenAPI (JSON)**

Si prefieres obtener la especificación en formato JSON:

- URL: `http://localhost:[puerto]/v3/api-docs`

## Características de Swagger

- **Documentación Interactiva**: Visualiza todos los endpoints disponibles
- **Pruebas Directas**: Realiza peticiones HTTP directamente desde la UI
- **Esquemas**: Ver los modelos de datos (DTOs) utilizados
- **Autenticación**: Configurable para endpoints protegidos
- **Exportación**: Descargar la especificación en formato OpenAPI

## Anotaciones Recomendadas para APIs

Para mejorar la documentación automática de tus endpoints, usa anotaciones de SpringDoc:

```java
@RestController
@RequestMapping("/api/estudiantes")
@Tag(name = "Estudiantes", description = "Operaciones de gestión de estudiantes")
public class EstudianteController {
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por ID")
    @ApiResponse(responseCode = "200", description = "Estudiante encontrado")
    public ResponseEntity<EstudianteDTO> obtenerPorId(@PathVariable Long id) {
        // implementación
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo estudiante")
    public ResponseEntity<EstudianteDTO> crear(@RequestBody EstudianteDTO dto) {
        // implementación
    }
}
```

## Notas Importantes

1. **Habilitar Swagger en Producción**: Por defecto, Swagger está habilitado. En producción, considera deshabilitarlo:
   ```properties
   springdoc.swagger-ui.enabled=false
   ```

2. **Seguridad**: Si implementas autenticación, configura Swagger para incluir tokens JWT:
   ```properties
   springdoc.swagger-ui.bearer-format=jwt
   ```

3. **Versionado**: La versión actual es "1.0". Actualízala en `SwaggerConfig.java` cuando hagas cambios significativos

## Estructura de Archivos Creados

```
servicio-[nombre]/
├── src/main/java/com/duoc/servicio_[nombre]/
│   └── config/
│       └── SwaggerConfig.java          (NUEVO)
└── src/main/resources/
    └── application.properties           (MODIFICADO)
```

## Soporte y Documentación Adicional

- **SpringDoc OpenAPI**: https://springdoc.org/
- **OpenAPI 3.0 Specification**: https://spec.openapis.org/oas/v3.0.3
- **Swagger UI**: https://swagger.io/tools/swagger-ui/

---

**Versión**: 1.0
**Fecha de Integración**: 2026-05-29
**Responsable**: GitHub Copilot

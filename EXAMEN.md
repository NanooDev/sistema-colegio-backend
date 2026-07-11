# 📚 EXAMEN FINAL - Sistema Colegio Backend
### DSY1103 - Desarrollo FullStack 1

> **Propósito:** Este documento es la guía definitiva de estudio para el examen oral/defensa técnica. Combina los conceptos de las **3 Experiencias de Aprendizaje** en escenarios integrados, tal como el profesor los puede pedir en tiempo real.

---

## 📋 Índice

1. [Resumen de las 3 Experiencias de Aprendizaje](#1-resumen-de-las-3-experiencias-de-aprendizaje)
2. [Arquitectura del Proyecto](#2-arquitectura-del-proyecto)
3. [Casos de Examen Integrados (EA1 + EA2 + EA3)](#3-casos-de-examen-integrados)
4. [Preguntas Frecuentes de Defensa](#4-preguntas-frecuentes-de-defensa)
5. [Guía Rápida de Código Listo para Copiar](#5-guía-rápida-de-código)
6. [Errores Comunes y Cómo Evitarlos](#6-errores-comunes)
7. [Checklist Pre-Examen](#7-checklist-pre-examen)

---

## 1. Resumen de las 3 Experiencias de Aprendizaje

### 🔵 Experiencia 1 — Fundamentos de Spring Boot y REST
**Temas clave:**
- Introducción a microservicios y arquitectura distribuida
- Patrón **CSR: Controller → Service → Repository/Model**
- Fundamentos de **HTTP y REST** (verbos GET, POST, PUT, DELETE; códigos de respuesta 200, 201, 400, 404, 409)
- Creación de proyectos con **Spring Boot**
- **Lombok** (reduce boilerplate con `@Data`, `@Getter`, `@Setter`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`)
- Control de versiones con **Git y GitHub**
- Pruebas con **Postman**

**Lo que implementamos:**
- Microservicios con sus entidades (`Estudiante`, `Profesor`, `Curso`, etc.)
- Endpoints REST básicos (CRUD)
- Estructura de paquetes: `controller/`, `service/`, `model/`, `repository/`, `dto/`, `exception/`, `config/`

---

### 🟢 Experiencia 2 — Persistencia, Comunicación y Validaciones
**Temas clave:**
- **Persistencia con JPA/Hibernate** y ORM
- Conexión a **MySQL** con Docker
- **Liquibase** para migraciones y seeds de BD
- Comunicación entre microservicios con **OpenFeign** (`@FeignClient`)
- **ResponseEntity** para control de respuestas HTTP
- **Validaciones** con Bean Validation (`@NotBlank`, `@Pattern`, `@NotNull`, etc.)
- **Manejo de errores** con excepciones personalizadas (`@ControllerAdvice`, `@ExceptionHandler`)
- **Logs** con SLF4J/Logback (`@Slf4j` + `log.info()`, `log.warn()`, `log.error()`)
- **Spring Security** básico
- Configuración YAML (`application.yml`) con **perfiles** (`dev`, `docker`)

**Lo que implementamos:**
- Base de datos propia por microservicio (una MySQL por servicio en Docker)
- `@FeignClient` en `servicio-cursos` para consultar estudiantes y profesores
- Endpoints `/detalle` que combinan datos de múltiples servicios
- Validaciones de RUT (regex), campos obligatorios, duplicados
- Excepciones custom: `EstudianteNotFoundException`, `EstudianteDuplicadoException`
- Configuración YAML con dos perfiles: base (local) y `docker`

---

### 🔴 Experiencia 3 — Testing, Documentación y Despliegue
**Temas clave:**
- **Pruebas unitarias** con JUnit 5 y Mockito
  - `@ExtendWith(MockitoExtension.class)`
  - `@Mock`, `@InjectMocks`
  - Estructura **Given-When-Then**
  - `assertNotNull`, `assertEquals`, `assertThrows`, `assertTrue`, `assertFalse`
  - `verify()`, `when().thenReturn()`, `never()`
- **Pruebas de Controller** con `@WebMvcTest` + `@MockitoBean` + `MockMvc`
- **Swagger / OpenAPI** (`springdoc-openapi`)
  - `@Tag`, `@Operation`, `@ApiResponse`, `@ApiResponses`, `@Schema`
  - Swagger federado en el Gateway
- **API Gateway** con Spring Cloud Gateway
  - Rutas (`predicates`, `filters`, `RewritePath`)
  - Load balancing con Eureka (`lb://NOMBRE-SERVICIO`)
- **Eureka** Service Discovery
- **Docker y Docker Compose** para despliegue
  - Dockerfile con Maven Wrapper
  - Perfiles `base` (local) vs `docker` (contenedores)

**Lo que implementamos:**
- 10 clases de test de Service con Given-When-Then
- 10 clases de test de Controller con MockMvc
- Swagger en los 10 microservicios + Swagger federado en el Gateway
- API Gateway enrutando los 10 servicios + docs de Swagger de cada uno
- Docker Compose levantando todo el ecosistema (12 contenedores)
- Logs en archivos `logs/*.log` configurados en YAML

---

## 2. Arquitectura del Proyecto

```
                        CLIENTE (Postman / Frontend)
                                   |
                    [API Gateway :8090] ← Eureka: lb://SERVICIO-X
                                   |
                    [Eureka Server :8761] ← todos los servicios se registran acá
                                   |
        ┌──────────┬──────────┬──────────┬──────────┬──────────┐
        |          |          |          |          |          |
 [Estudiantes] [Profesores] [Cursos] [Asignaturas] [Calificaciones] ...
    :8081        :8082       :8084     :8085         :8095
     MySQL        MySQL      MySQL      MySQL         MySQL
    :3311         :3312      :3314      :3315         :3320
```

**Flujo de una petición:**
1. Cliente llama a `GET http://localhost:8090/api/v1/cursos/1`
2. El Gateway consulta a **Eureka** dónde está `SERVICIO-CURSOS`
3. Eureka devuelve la dirección de `servicio-cursos` (ej. `localhost:8084`)
4. El Gateway hace **proxy** de la petición al servicio real
5. `servicio-cursos` usa **Feign** para pedir datos a `servicio-estudiantes` y `servicio-profesores`
6. Se arma la respuesta enriquecida y se devuelve al cliente

---

## 3. Casos de Examen Integrados

> ⚠️ El profesor pedirá algo que involucre **las 3 experiencias al mismo tiempo**. Estudia estos casos y practica escribir el código de memoria.

---

### 🎯 CASO 1: "Agrega un log de Info cuando se crea un estudiante, y verifica que el endpoint aparece en Swagger"

**Conceptos involucrados:**
- **EA1** → Patrón CSR, endpoint POST REST
- **EA2** → Logs con `@Slf4j`, `log.info()`
- **EA3** → Swagger con `@Operation`, `@ApiResponse`

**Paso a paso:**

**1. En `EstudianteService.java`** — agregar `@Slf4j` y el log:
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j   // ← EA2: esto genera el logger automáticamente con Lombok
@Service
public class EstudianteService {

    public EstudianteDTO guardar(EstudianteRequest request) {
        // EA2: validar duplicado (regla de negocio)
        if (estudianteRepository.existsByRut(request.getRut())) {
            log.warn("Intento de registro con RUT duplicado: {}", request.getRut()); // ← log warn
            throw new EstudianteDuplicadoException(request.getRut());
        }

        log.info("Creando estudiante con RUT: {}", request.getRut()); // ← EA2: log info
        Estudiante estudiante = new Estudiante();
        estudiante.setRut(request.getRut());
        estudiante.setNombre(request.getNombre());
        // ... resto de setters

        EstudianteDTO resultado = convertirADTO(estudianteRepository.save(estudiante));
        log.info("Estudiante creado exitosamente con ID: {}", resultado.getId()); // ← log info
        return resultado;
    }
}
```

**2. En `EstudianteController.java`** — verificar/agregar Swagger:
```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/estudiantes")
@Tag(name = "Estudiantes", description = "Operaciones CRUD para estudiantes") // ← EA3: Swagger
public class EstudianteController {

    @Operation(summary = "Crear un estudiante", description = "Crea un nuevo estudiante. Valida RUT único.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "RUT ya existe")
    })
    @PostMapping
    public ResponseEntity<EstudianteDTO> guardar(@Valid @RequestBody EstudianteRequest request) {
        return new ResponseEntity<>(estudianteService.guardar(request), HttpStatus.CREATED);
    }
}
```

**3. Verificar en Swagger:**
- Ir a `http://localhost:8081/doc/swagger-ui.html` → debe aparecer el endpoint POST documentado
- O en el Gateway: `http://localhost:8090/doc/swagger-ui.html` → seleccionar "estudiantes" en el dropdown

**4. Cómo los logs se ven:**
- En consola al ejecutar el servicio
- En el archivo `logs/servicio-estudiantes.log`

---

### 🎯 CASO 2: "Crea un nuevo endpoint en Cursos que devuelva cuántos estudiantes tiene un curso, que sea accesible por el Gateway y que tenga test unitario"

**Conceptos involucrados:**
- **EA1** → Nuevo endpoint REST en el controller
- **EA2** → Comunicación entre servicios con FeignClient para contar estudiantes
- **EA3** → Test unitario Given-When-Then + que el Gateway lo enrute

**Paso a paso:**

**1. En `CursoService.java`** — nuevo método que usa Feign:
```java
@Slf4j
@Service
public class CursoService {

    @Autowired
    private EstudianteFeign estudianteFeign; // ← EA2: Feign Client

    // ... métodos existentes

    // NUEVO MÉTODO
    public int contarEstudiantesDeCurso(Long cursoId) {
        log.info("Consultando cantidad de estudiantes para curso ID: {}", cursoId); // ← EA2: log
        try {
            List<EstudianteDTO> estudiantes = estudianteFeign.obtenerEstudiantesPorCurso(cursoId);
            int total = estudiantes != null ? estudiantes.size() : 0;
            log.info("Curso {} tiene {} estudiantes", cursoId, total);
            return total;
        } catch (Exception e) {
            log.error("Error al consultar estudiantes del curso {}: {}", cursoId, e.getMessage()); // ← log error
            return 0; // fallback (EA2: manejo de error remoto)
        }
    }
}
```

**2. En `CursoController.java`** — nuevo endpoint con Swagger:
```java
@Operation(summary = "Contar estudiantes de un curso", description = "Devuelve el total de estudiantes asignados al curso")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Conteo exitoso"),
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
})
@GetMapping("/{id}/cantidad-estudiantes")
public ResponseEntity<Map<String, Object>> contarEstudiantes(@PathVariable Long id) {
    int cantidad = cursoService.contarEstudiantesDeCurso(id);
    Map<String, Object> respuesta = new HashMap<>();
    respuesta.put("cursoId", id);
    respuesta.put("totalEstudiantes", cantidad);
    return ResponseEntity.ok(respuesta);
}
```

**3. En el Gateway (`application.yml`)** — ya está cubierto porque la ruta `/api/v1/cursos/**` ya enruta todo:
```yaml
- id: ruta-cursos
  uri: lb://SERVICIO-CURSOS
  predicates:
    - Path=/api/v1/cursos/**   # ← el ** cubre /cursos/{id}/cantidad-estudiantes
```

**4. Test unitario del nuevo método (`CursoServiceTest.java`):**
```java
@Test
void contarEstudiantesDeCurso_cuandoHayEstudiantes_deberiaRetornarCantidad() {
    // GIVEN (EA3: preparar datos mock)
    Long cursoId = 1L;
    List<EstudianteDTO> listaFake = List.of(new EstudianteDTO(), new EstudianteDTO());
    when(estudianteFeign.obtenerEstudiantesPorCurso(cursoId)).thenReturn(listaFake);

    // WHEN (EA3: ejecutar el método real)
    int resultado = cursoService.contarEstudiantesDeCurso(cursoId);

    // THEN (EA3: verificar resultado)
    assertEquals(2, resultado);
    verify(estudianteFeign, times(1)).obtenerEstudiantesPorCurso(cursoId); // ← verify
}

@Test
void contarEstudiantesDeCurso_cuandoFeignFalla_deberiaRetornarCero() {
    // GIVEN: simular falla del servicio remoto (EA2: fallback)
    Long cursoId = 99L;
    when(estudianteFeign.obtenerEstudiantesPorCurso(cursoId)).thenThrow(new RuntimeException("Servicio caído"));

    // WHEN
    int resultado = cursoService.contarEstudiantesDeCurso(cursoId);

    // THEN: fallback devuelve 0 (no explota)
    assertEquals(0, resultado);
}
```

---

### 🎯 CASO 3: "Agrega una validación al crear un Profesor: el nombre no puede ser vacío, debe registrarse en Eureka y debe aparecer documentado en Swagger"

**Conceptos involucrados:**
- **EA1** → Estructura CSR, endpoint POST
- **EA2** → Validaciones con `@NotBlank`, `@Pattern`, manejo de errores 400
- **EA3** → Swagger con `@Schema` en el DTO, registro en Eureka (automático)

**Paso a paso:**

**1. En `ProfesorRequest.java`** — agregar validaciones y Swagger:
```java
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfesorRequest {

    @NotBlank(message = "El nombre no puede estar vacío")     // ← EA2: validación
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del profesor", example = "María")  // ← EA3: Swagger
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Schema(description = "Apellido del profesor", example = "González")
    private String apellido;

    @NotBlank(message = "La especialidad no puede estar vacía")
    @Schema(description = "Especialidad del profesor", example = "Matemáticas")
    private String especialidad;
}
```

**2. En `ProfesorController.java`** — asegurarse de usar `@Valid`:
```java
@PostMapping
public ResponseEntity<ProfesorDTO> crear(@Valid @RequestBody ProfesorRequest request) {
    // @Valid activa las anotaciones de validación del DTO ← EA2
    return new ResponseEntity<>(profesorService.guardar(request), HttpStatus.CREATED);
}
```

**3. En `application.yml`** — Eureka está configurado (automático):
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/   # ← EA3: registro automático
  instance:
    prefer-ip-address: true
```

**4. ¿Cómo verificar que está en Eureka?**
- Abrir `http://localhost:8761` y ver que `SERVICIO-PROFESORES` aparece como instancia registrada

**5. Test unitario validando la regla:**
```java
@Test
void guardar_cuandoProfesorEsValido_deberiaRetornarDTO() {
    // GIVEN
    ProfesorRequest request = new ProfesorRequest();
    request.setNombre("María");
    request.setApellido("González");
    request.setEspecialidad("Matemáticas");

    Profesor guardado = new Profesor();
    guardado.setId(1L);
    guardado.setNombre("María");

    when(profesorRepository.save(any(Profesor.class))).thenReturn(guardado);

    // WHEN
    ProfesorDTO resultado = profesorService.guardar(request);

    // THEN
    assertNotNull(resultado);
    assertEquals("María", resultado.getNombre());
    verify(profesorRepository, times(1)).save(any(Profesor.class));
}
```

---

### 🎯 CASO 4: "Modifica el API Gateway para agregar una nueva ruta hacia un nuevo microservicio, registralo en Eureka y que tenga un log cuando procese peticiones"

**Conceptos involucrados:**
- **EA1** → Nuevo microservicio con patrón CSR básico
- **EA2** → Logs en el Service, registro en Eureka con YAML
- **EA3** → Configuración del Gateway con nueva ruta

**Paso a paso:**

**1. En `api-gateway/application.yml`** — agregar la nueva ruta:
```yaml
spring:
  cloud:
    gateway:
      server:
        webflux:
          routes:
            # ... rutas existentes ...
            - id: ruta-nuevo-servicio          # ← nombre único de la ruta
              uri: lb://SERVICIO-NUEVO         # ← EA3: load-balancing via Eureka
              predicates:
                - Path=/api/v1/nuevo/**        # ← patrón de URL que captura

            # También agregar la ruta del API doc para Swagger federado:
            - id: nuevo-api-docs
              uri: lb://SERVICIO-NUEVO
              predicates:
                - Path=/v3/api-docs/nuevo
              filters:
                - RewritePath=/v3/api-docs/nuevo, /v3/api-docs
```

**2. En `application.yml` del nuevo microservicio** — configurar Eureka y logs:
```yaml
spring:
  application:
    name: servicio-nuevo    # ← este nombre debe coincidir con lb://SERVICIO-NUEVO

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/   # ← EA2: se registra en Eureka

logging:
  level:
    root: info
    com.duoc: info
  file:
    name: "logs/servicio-nuevo.log"               # ← EA2: logs a archivo

server:
  port: 8091   # puerto único, no colisionar con otros servicios
```

**3. En `NuevoService.java`** — log de cada petición procesada:
```java
@Slf4j     // ← EA2: anotación Lombok para logger
@Service
public class NuevoService {

    public List<NuevoDTO> listar() {
        log.info("[NuevoService] Procesando petición: listar todos");  // ← EA2: log
        List<NuevoDTO> resultado = // ... lógica ...
        log.info("[NuevoService] Retornando {} elementos", resultado.size());
        return resultado;
    }
}
```

**4. Verificar el flujo completo:**
```
Postman → GET http://localhost:8090/api/v1/nuevo
    → Gateway (puerto 8090)
    → Consulta Eureka → encuentra SERVICIO-NUEVO en :8091
    → Redirige petición al microservicio
    → Service procesa y loguea
    → Respuesta vuelve al cliente
```

---

### 🎯 CASO 5: "Crea un test unitario para un método que lanza excepción cuando el estudiante no existe, y explica cada línea"

**Conceptos involucrados:**
- **EA1** → Patrón CSR, entiende qué es el Service
- **EA2** → Excepción personalizada `EstudianteNotFoundException`
- **EA3** → Test con Given-When-Then, `assertThrows`, `@Mock`, `@InjectMocks`

**Código completo del test:**
```java
@ExtendWith(MockitoExtension.class)   // ← EA3: activa Mockito para esta clase de test
class EstudianteServiceTest {

    @Mock                              // ← EA3: crea un "doble de prueba" del repositorio
    private EstudianteRepository estudianteRepository;

    @InjectMocks                       // ← EA3: crea el Service real e inyecta el @Mock
    private EstudianteService estudianteService;

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // GIVEN (preparar el escenario) ← EA3: Given
        Integer idInexistente = 999;
        when(estudianteRepository.findById(idInexistente))  // cuando se llame findById(999)...
            .thenReturn(Optional.empty());                  // ...devolver vacío (simular que no existe)

        // WHEN & THEN (ejecutar y verificar) ← EA3: When + Then
        assertThrows(
            EstudianteNotFoundException.class,              // ← EA2: excepción que esperamos
            () -> estudianteService.buscarPorId(idInexistente)  // ejecutar el método real
        );

        // Verificar que el repositorio fue llamado exactamente 1 vez
        verify(estudianteRepository, times(1)).findById(idInexistente);
    }
}
```

**Explicación línea por línea para la defensa:**
- `@ExtendWith(MockitoExtension.class)` → Le dice a JUnit que use las extensiones de Mockito para esta clase
- `@Mock` → Crea un objeto falso del repositorio. No toca la BD real
- `@InjectMocks` → Crea el `EstudianteService` real e inyecta todos los `@Mock` en sus dependencias
- `when(...).thenReturn(...)` → Programa el comportamiento del mock: "cuando se llame este método, devuelve esto"
- `assertThrows(...)` → Verifica que se lanzó la excepción esperada. Si NO se lanza, el test falla
- `verify(...)` → Verifica que el repositorio fue llamado con los parámetros correctos, el número correcto de veces

---

### 🎯 CASO 6: "Agrega un endpoint de detalle en Matrícula que consulte el nombre del estudiante vía Feign, con log y documentado en Swagger"

**Conceptos involucrados:**
- **EA1** → Endpoint REST, patrón CSR
- **EA2** → FeignClient para consumir otro microservicio, manejo de error remoto, logs
- **EA3** → Swagger con `@Operation`, `@Schema` en DTO

**Paso a paso:**

**1. `EstudianteFeign.java`** en `servicio-matriculas/client/`:
```java
@FeignClient(name = "servicio-estudiantes", path = "/api/v1/estudiantes")  // ← EA2: Feign
public interface EstudianteFeign {

    @GetMapping("/{id}")
    EstudianteDTO buscarPorId(@PathVariable("id") Integer id);
}
```

**2. En `MatriculaService.java`** — método detalle:
```java
@Slf4j
@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudianteFeign estudianteFeign;  // ← EA2: Feign inyectado

    public MatriculaDetalleDTO obtenerDetalle(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
            .orElseThrow(() -> new MatriculaNotFoundException(id));  // ← EA2: excepción

        log.info("Buscando detalle de matrícula ID: {}", id);  // ← EA2: log info

        MatriculaDetalleDTO dto = new MatriculaDetalleDTO();
        dto.setId(matricula.getId());
        dto.setEstudianteId(matricula.getEstudianteId());

        // Consultar nombre del estudiante al otro servicio
        try {
            EstudianteDTO estudiante = estudianteFeign.buscarPorId(matricula.getEstudianteId().intValue());
            dto.setNombreEstudiante(estudiante.getNombre() + " " + estudiante.getApellido());
            log.info("Nombre del estudiante obtenido: {}", dto.getNombreEstudiante());
        } catch (Exception e) {
            log.warn("No se pudo obtener nombre del estudiante: {}", e.getMessage());  // ← fallback
            dto.setNombreEstudiante("Desconocido");  // ← EA2: degradación elegante
        }
        return dto;
    }
}
```

**3. En `MatriculaController.java`** — nuevo endpoint:
```java
@Operation(summary = "Obtener matrícula con detalle del estudiante")  // ← EA3: Swagger
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Detalle obtenido exitosamente"),
    @ApiResponse(responseCode = "404", description = "Matrícula no encontrada")
})
@GetMapping("/{id}/detalle")
public ResponseEntity<MatriculaDetalleDTO> obtenerDetalle(@PathVariable Long id) {
    return ResponseEntity.ok(matriculaService.obtenerDetalle(id));
}
```

**4. `MatriculaDetalleDTO.java`** con `@Schema`:
```java
@Data
public class MatriculaDetalleDTO {
    @Schema(description = "ID de la matrícula", example = "1")
    private Long id;

    @Schema(description = "ID del estudiante", example = "5")
    private Long estudianteId;

    @Schema(description = "Nombre completo del estudiante", example = "Juan Pérez")
    private String nombreEstudiante;  // ← viene del servicio externo via Feign
}
```

---

### 🎯 CASO 7: "Explica cómo funciona el YAML del Gateway y modifica el puerto del Gateway de 8090 a 9090"

**Conceptos involucrados:**
- **EA2** → YAML, perfiles de configuración
- **EA3** → Configuración del Gateway

**El YAML del Gateway explicado:**
```yaml
spring:
  application:
    name: api-gateway          # Nombre con que se registra en Eureka
  cloud:
    gateway:
      server:
        webflux:
          globalcors:          # Permite peticiones desde cualquier origen (CORS)
            cors-configurations:
              '[/**]':
                allowedOrigins: "*"
                allowedMethods: "*"
                allowedHeaders: "*"
          routes:
            - id: ruta-estudiantes          # Nombre único de la ruta
              uri: lb://SERVICIO-ESTUDIANTES # lb:// = load balancing via Eureka
              predicates:
                - Path=/api/v1/estudiantes/** # Captura todas las sub-rutas

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/  # Dónde está Eureka

server:
  port: ${GATEWAY_PORT:8090}   # Puerto por variable de entorno, o 8090 por defecto

---              # ← Separador de perfil
spring:
  config:
    activate:
      on-profile: docker       # Este bloque solo se activa con perfil "docker"
eureka:
  client:
    service-url:
      defaultZone: http://eureka:8761/eureka/  # Hostname interno de Docker
```

**Cambiar el puerto a 9090:**
```yaml
server:
  port: 9090   # ← cambiar aquí en application.yml del gateway
```
```yaml
# En docker-compose.yml:
api-gateway:
  ports:
    - "9090:9090"   # ← actualizar también aquí
```

---

## 4. Preguntas Frecuentes de Defensa

### ❓ ¿Qué es el patrón CSR y por qué se usa?
> **Controller** solo recibe la petición HTTP y devuelve la respuesta — no tiene lógica de negocio.
> **Service** contiene toda la lógica de negocio (validaciones, reglas, transformaciones).
> **Repository** solo se comunica con la base de datos.
>
> Se usa para **separar responsabilidades**: si mañana cambias de MySQL a MongoDB, solo tocas el Repository. Si cambias las reglas de negocio, solo tocas el Service.

---

### ❓ ¿Qué es Eureka y para qué sirve?
> Eureka es un **servidor de descubrimiento de servicios**. En lugar de escribir en código la IP y puerto de cada microservicio (hardcoded), cada servicio se "anuncia" en Eureka al arrancar. Cuando el Gateway necesita redirigir una petición a `SERVICIO-ESTUDIANTES`, le pregunta a Eureka dónde está, y Eureka devuelve la IP y puerto real. Esto permite escalabilidad y que los servicios cambien de puerto sin romper el sistema.

---

### ❓ ¿Qué es un FeignClient y cómo funciona?
> Es una interfaz declarativa que Spring convierte en un cliente HTTP. En lugar de escribir código para hacer peticiones REST, defines la interfaz como si fuera un controller local:
> ```java
> @FeignClient(name = "servicio-estudiantes", path = "/api/v1/estudiantes")
> public interface EstudianteFeign {
>     @GetMapping("/{id}")
>     EstudianteDTO buscarPorId(@PathVariable("id") Integer id);
> }
> ```
> Spring se encarga de hacer la petición HTTP real. Usa Eureka para resolver el nombre del servicio.

---

### ❓ ¿Por qué usamos `when(...).thenReturn(...)` en los tests?
> Porque el Service depende del Repository para acceder a la BD. En un test unitario **no queremos conectarnos a la BD real** (lento, frágil, necesita infraestructura). Con `@Mock` creamos un "doble" del repository, y con `when(...).thenReturn(...)` programamos exactamente qué debe devolver cuando se le llame, para poder verificar que el Service se comporta correctamente.

---

### ❓ ¿Qué diferencia hay entre `@Mock` e `@InjectMocks`?
> - `@Mock` → Crea el objeto FALSO (el doble de prueba, ej. `EstudianteRepository` falso)
> - `@InjectMocks` → Crea el objeto REAL (ej. `EstudianteService`) e **inyecta automáticamente** todos los `@Mock` que coincidan con sus dependencias

---

### ❓ ¿Qué hace `verify()` en Mockito?
> Verifica que un método del mock fue llamado con ciertos parámetros y un número específico de veces. Esto prueba que el Service **realmente llama** al Repository:
> ```java
> verify(estudianteRepository, times(1)).save(any(Estudiante.class));  // llamado exactamente 1 vez
> verify(estudianteRepository, never()).save(any());                   // NUNCA fue llamado
> ```

---

### ❓ ¿Para qué sirve Swagger/OpenAPI?
> Genera documentación **interactiva** de la API. Otros desarrolladores (frontend, otros equipos) pueden ver qué endpoints existen, qué parámetros reciben, qué respuestas devuelven, y probarlos directamente desde el navegador sin Postman. Las anotaciones `@Tag`, `@Operation`, `@ApiResponse`, `@Schema` enriquecen esa documentación.

---

### ❓ ¿Qué son los perfiles de Spring?
> Permiten tener configuraciones diferentes según el entorno. En nuestro proyecto:
> - **Perfil base** (sin perfil): usa `localhost:3311-3320` para conectarse a las BD — para desarrollo local
> - **Perfil `docker`**: usa `mysql-servicio-X:3306` (hostnames internos de Docker) — para contenedores
>
> Docker Compose activa el perfil docker con `SPRING_PROFILES_ACTIVE=docker`.

---

### ❓ ¿Cómo funciona Liquibase?
> Es una herramienta de migración de base de datos. En lugar de crear las tablas manualmente, defines los cambios en archivos XML/SQL llamados "changesets". Liquibase los aplica automáticamente al arrancar la app. También usamos Liquibase para los **seeds** (datos iniciales que se insertan para poder probar).

---

### ❓ ¿Qué pasa si un FeignClient falla?
> En nuestro proyecto implementamos **degradación elegante** (fallback manual): si el servicio remoto no responde, el catch captura la excepción y devuelve un valor por defecto (ej. `"Desconocido"` para el nombre, o `0` para la cantidad). Así el endpoint del servicio que hace la consulta sigue funcionando, aunque con información parcial.

---

### ❓ ¿Cómo funciona el `lb://` en el Gateway?
> El prefijo `lb://` le indica al Gateway que debe usar **Load Balancer** de Spring Cloud para resolver el nombre del servicio consultando a Eureka. El nombre en MAYÚSCULAS (`lb://SERVICIO-ESTUDIANTES`) debe coincidir con el `spring.application.name` registrado en Eureka (aunque Eureka convierte a mayúsculas automáticamente).

---

## 5. Guía Rápida de Código

### 🔧 Agregar `@Slf4j` y logs a cualquier clase

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j  // ← genera: private static final Logger log = LoggerFactory.getLogger(MiClase.class);
@Service
public class MiService {
    public void miMetodo(String dato) {
        log.info("Procesando: {}", dato);           // {} es el placeholder
        log.warn("Advertencia: {}", dato);
        log.error("Error crítico: {}", e.getMessage());
        log.debug("Debug: valor = {}", valor);
    }
}
```

---

### 🔧 Crear un FeignClient desde cero

```java
// 1. En el pom.xml (ya debe estar en el proyecto):
// <dependency>
//     <groupId>org.springframework.cloud</groupId>
//     <artifactId>spring-cloud-starter-openfeign</artifactId>
// </dependency>

// 2. En la Application class:
@SpringBootApplication
@EnableFeignClients   // ← OBLIGATORIO para activar Feign
public class ServicioXApplication { }

// 3. La interfaz Feign:
@FeignClient(name = "servicio-estudiantes", path = "/api/v1/estudiantes")
public interface EstudianteFeign {
    @GetMapping("/{id}")
    EstudianteDTO buscarPorId(@PathVariable("id") Integer id);

    @GetMapping("/curso/{cursoId}")
    List<EstudianteDTO> obtenerPorCurso(@PathVariable("cursoId") Long cursoId);
}

// 4. En el Service, inyectar con @Autowired:
@Autowired
private EstudianteFeign estudianteFeign;
```

---

### 🔧 Test de Service completo (estructura base)

```java
@ExtendWith(MockitoExtension.class)
class MiServiceTest {

    @Mock
    private MiRepository miRepository;

    // Si el service tiene Feign también se mockea:
    @Mock
    private OtroServiceFeign otroFeign;

    @InjectMocks
    private MiService miService;

    @Test
    void miMetodo_cuandoCondicion_deberiaResultado() {
        // GIVEN
        MiEntidad entidad = new MiEntidad();
        entidad.setId(1L);
        when(miRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // WHEN
        MiDTO resultado = miService.buscarPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(miRepository, times(1)).findById(1L);
    }

    @Test
    void miMetodo_cuandoNoExiste_deberiaLanzarExcepcion() {
        // GIVEN
        when(miRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(MiNotFoundException.class, () -> miService.buscarPorId(999L));
        verify(miRepository, never()).save(any());
    }
}
```

---

### 🔧 Swagger Config en cualquier microservicio

```java
// SwaggerConfig.java en paquete config/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Servicio de Estudiantes")
                        .version("1.0")
                        .description("Documentación de la API del Servicio de Estudiantes - Colegio"));
    }
}
```

```yaml
# En application.yml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    path: /doc/swagger-ui.html
```

---

### 🔧 Agregar ruta al Gateway

```yaml
# En api-gateway/application.yml, dentro de routes:
- id: ruta-nuevo            # nombre único, sin espacios
  uri: lb://SERVICIO-NUEVO  # mayúsculas, debe coincidir con spring.application.name
  predicates:
    - Path=/api/v1/nuevo/** # ** cubre todo lo que venga después
```

---

### 🔧 Excepción personalizada (patrón del proyecto)

```java
// 1. La excepción:
public class EstudianteNotFoundException extends RuntimeException {
    public EstudianteNotFoundException(Integer id) {
        super("Estudiante no encontrado con id: " + id);
    }
}

// 2. El handler global (en paquete exception/):
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstudianteNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EstudianteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EstudianteDuplicadoException.class)
    public ResponseEntity<Map<String, String>> handleDuplicado(EstudianteDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
            errores.put(err.getField(), err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }
}
```

---

## 6. Errores Comunes

| ❌ Error | ✅ Solución |
|---|---|
| `@EnableFeignClients` no está en la Application class | Agregar `@EnableFeignClients` encima de `@SpringBootApplication` |
| Gateway devuelve 503 | El microservicio no está registrado en Eureka — verificar que esté corriendo |
| Test falla con `NullPointerException` en el service | Falta `@InjectMocks` o `@Mock` — revisar que el mock coincida con la dependencia |
| Swagger no muestra el endpoint | Verificar que `springdoc.swagger-ui.enabled=true` esté en el YAML |
| Error 409 Conflict al crear | El RUT ya existe — usar un RUT diferente o limpiar la BD |
| Feign devuelve null | El servicio remoto no está levantado — verificar Docker o el proceso corriendo |
| `lb://servicio-x` no resuelve | El nombre debe ser MAYÚSCULAS: `lb://SERVICIO-X` |
| Logs no aparecen en archivo | Verificar la configuración `logging.file.name` en el YAML |
| Test de controller falla | Verificar que se use `@MockitoBean` (no `@Mock`) y `@WebMvcTest` |
| Perfil docker no conecta a BD | El hostname debe ser `mysql-servicio-X` (no `localhost`) en el perfil docker |

---

## 7. Checklist Pre-Examen

### ✅ Antes de la defensa, verificar:

**Proyecto:**
- [ ] `docker compose up -d --build` levanta todo sin errores
- [ ] Eureka en `http://localhost:8761` muestra **11 instancias** (Gateway + 10 servicios)
- [ ] Swagger del Gateway en `http://localhost:8090/doc/swagger-ui.html` muestra todos los servicios en el dropdown
- [ ] `GET http://localhost:8090/api/v1/cursos/1` devuelve curso con profesor y estudiantes (prueba de Feign)

**Tests:**
- [ ] `./mvnw test` en cualquier microservicio pasa sin errores
- [ ] Sabes explicar Given-When-Then de cualquier test
- [ ] Sabes qué valida cada `assert`

**Concepto a dominar por EA:**
- [ ] **EA1**: Puedo crear un endpoint POST/GET en un Controller desde cero
- [ ] **EA2**: Puedo agregar un FeignClient que consulte otro microservicio, y manejar el error si falla
- [ ] **EA3**: Puedo escribir un test unitario con Given-When-Then, `@Mock`, `@InjectMocks`

**Para el examen oral:**
- [ ] Sé explicar qué hace cada capa del patrón CSR
- [ ] Sé explicar qué es Eureka, el Gateway y Feign con palabras simples
- [ ] Sé navegar el Swagger y explicar cada campo
- [ ] Sé leer el `application.yml` y explicar cada sección
- [ ] Puedo modificar el código en tiempo real sin googlear la estructura básica

---

## 📂 Estructura del Proyecto

```
sistema-colegio-backend/
├── eureka/                        ← Servidor Eureka (puerto 8761)
├── api-gateway/                   ← API Gateway (puerto 8090)
│   └── src/main/resources/
│       └── application.yml        ← Rutas, CORS, Swagger federado, perfiles
├── servicio-estudiantes/          ← Puerto 8081
│   └── src/
│       ├── main/java/.../
│       │   ├── controller/        ← EstudianteController.java
│       │   ├── service/           ← EstudianteService.java
│       │   ├── repository/        ← EstudianteRepository.java
│       │   ├── model/             ← Estudiante.java (entidad JPA)
│       │   ├── dto/               ← EstudianteDTO.java, EstudianteRequest.java
│       │   ├── exception/         ← NotFoundException, GlobalExceptionHandler
│       │   └── config/            ← SwaggerConfig.java
│       └── test/java/.../
│           ├── service/           ← EstudianteServiceTest.java (Given-When-Then)
│           └── controller/        ← EstudianteControllerTest.java (MockMvc)
├── servicio-cursos/               ← Puerto 8084 (tiene FeignClients)
│   └── src/main/java/.../
│       └── client/                ← EstudianteFeign.java, ProfesorFeign.java
├── ... (8 microservicios más con misma estructura)
├── docker-compose.yml             ← Orquesta los 12 contenedores
├── EXAMEN.md                      ← Este archivo
├── ev3.md                         ← Guía de cambios rápidos para defensa
├── README.md                      ← Documentación general del proyecto
├── TESTING.md                     ← Datos de prueba con Postman
└── SWAGGER_INTEGRATION.md        ← Detalle de Swagger
```

---

## 🔗 URLs de Referencia Rápida

| Recurso | URL |
|---|---|
| Eureka Dashboard | http://localhost:8761 |
| Swagger Gateway (todos los servicios) | http://localhost:8090/doc/swagger-ui.html |
| Swagger Estudiantes | http://localhost:8081/doc/swagger-ui.html |
| Swagger Profesores | http://localhost:8082/doc/swagger-ui.html |
| Swagger Cursos | http://localhost:8084/doc/swagger-ui.html |
| API Estudiantes | http://localhost:8090/api/v1/estudiantes |
| API Cursos (con Feign) | http://localhost:8090/api/v1/cursos/1 |
| API Matrículas detalle | http://localhost:8090/api/v1/matriculas/1/detalle |

---

> 💡 **Tip final:** El profesor puede pedir cualquier combinación. Lo clave es que sepas cómo cada pieza se conecta: el Controller llama al Service, el Service usa el Repository y/o el FeignClient, el FeignClient habla con otro microservicio que el Gateway conoce gracias a Eureka. Si entiendes ese flujo completo, puedes responder cualquier pregunta.

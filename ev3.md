# Sistema Colegio Backend - Evaluación 3 (EV3)

Este documento detalla todas las integraciones y sirve como **Guía Oficial** para tu Defensa Técnica.

---

## 🚀 1. Guía de Encendido (Cómo levantar el proyecto)

El proyecto está orquestado para ser levantado en un solo comando mediante `docker-compose`.

### Prerrequisitos
- Tener **Docker Desktop** (Windows/Mac) o **Docker Daemon + Compose** (Linux).
- Tener los puertos libres: `3307` (MySQL), `8761` (Eureka) y `8090` (API Gateway).

### Pasos para levantar
1. Abre una terminal en la raíz del proyecto.
2. Limpia contenedores previos (opcional):
   ```bash
   docker compose down -v
   ```
3. Construye e inicia todos los servicios en segundo plano:
   ```bash
   docker compose up --build -d
   ```
   *Nota: El `--build` fuerza a Docker a compilar los `.jar` con los últimos cambios.*

### Verificación
1. Ingresa a [http://localhost:8761](http://localhost:8761) (Eureka). Debes ver **11 instancias** registradas (API-GATEWAY + 10 microservicios).
2. Para probar Swagger, ingresa a: `http://localhost:8081/doc/swagger-ui.html` (o el puerto del servicio que desees revisar).
3. Para probar HATEOAS, haz un GET a `http://localhost:8081/api/v2/carreras`.

---

## 📖 2. Guía de Estudio (Para la Defensa Oral)

Aquí tienes el resumen de todos los conceptos implementados en la Unidad 3 que debes dominar para explicarle al profesor.

### 🧪 3.1 Pruebas Unitarias (JUnit y Mockito)
- **¿Qué son?** Pruebas que validan unidades individuales de código (generalmente los Services) aislando las dependencias.
- **Given-When-Then:** Estructura que usamos. 
  - *Given* (Dado): Preparamos los datos mock.
  - *When* (Cuando): Llamamos al método real a probar.
  - *Then* (Entonces): Verificamos los resultados con `asserts` (ej. `assertNotNull`, `assertEquals`).
- **Mockito:** Se usa para simular (`@Mock`) el repositorio para no conectarnos a la base de datos real.
- **Testing Plan:** Hemos documentado las reglas críticas y su cobertura en el archivo `TESTING_PLAN.md`. Esto demuestra profesionalismo al detectar qué casos faltan probar (Deuda Técnica).

### 📝 3.2 Swagger y OpenAPI
- **Propósito:** Generar documentación interactiva de la API para que otros desarrolladores sepan cómo consumirla.
- **Anotaciones clave que usamos:**
  - `@Tag`: Agrupa endpoints por categoría (ej. "Carreras").
  - `@Operation`: Describe qué hace un endpoint específico.
  - `@ApiResponse`: Documenta los posibles códigos de respuesta (200, 404, etc.).
- **Configuración:** Usamos `springdoc-openapi-starter-webmvc-ui` y definimos una clase `@Configuration` con un `@Bean` que retorna un `OpenAPI` personalizado.

### 🌐 HATEOAS (Hypermedia As The Engine Of Application State)
- **¿Qué es?** Permite que las respuestas de la API devuelvan no solo los datos, sino también **enlaces (links)** hacia otras acciones o recursos relacionados.
- **¿Cómo lo implementamos?**
  - Añadimos `spring-boot-starter-hateoas`.
  - Creamos un **Assembler** (`CarreraModelAssembler`) que implementa `RepresentationModelAssembler`.
  - En los controladores V2, devolvemos `EntityModel` (para un objeto) o `CollectionModel` (para listas), los cuales incluyen los metadatos de hipermedia generados usando `linkTo` y `methodOn`.

### 👥 DataFaker y Perfiles (Profiles) de Spring
- **DataFaker:** Es una librería que usamos (`net.datafaker`) para poblar nuestra base de datos con datos de prueba automáticamente al iniciar el proyecto. Lo implementamos en la clase `DataLoader` (que implementa `CommandLineRunner`).
- **Perfiles (`@Profile`):** Dividimos nuestra configuración en dos archivos: `application-dev.properties` (para desarrollo local, donde se activa el `DataLoader` y guardamos en una BD de dev) y `application-test.properties` (para ejecutar las pruebas unitarias limpiamente). Elegimos cuál usar con `spring.profiles.active=dev` o `test`.

### 🚪 3.3 API Gateway, Eureka y YAML
- **API Gateway:** Centraliza el enrutamiento. Todas las peticiones del frontend entran por el Gateway (puerto 8090) y este decide a qué microservicio redirigir.
- **Eureka (Service Discovery):** Registro centralizado. Los microservicios se anuncian allí y el Gateway le pregunta a Eureka dónde está cada servicio (para no usar IPs quemadas en código, sino nombres como `lb://SERVICIO-ESTUDIANTES`).
- **YAML (`application.properties` / `application.yml`):** Define propiedades dependientes del entorno (ej. puertos, credenciales de BD, rutas de Eureka).

---

## ⚡ 3. Guía de Cambios Rápidos (Cheat Sheet para la Defensa)

Si el profesor te dice: *"Hazme este cambio en vivo para demostrar que tú hiciste el código"*.

### Caso 1: "Agrega una nueva prueba unitaria"
1. Ve a `src/test/java/.../service/` del microservicio que elijas.
2. Copia una prueba existente, por ejemplo `testFindById()`.
3. Cambia el nombre a `testFindById_NotFound()` y modifica el comportamiento del Mock:
   ```java
   when(repository.findById(999)).thenReturn(Optional.empty());
   Reserva result = service.findById(999);
   assertNull(result); // O lanza excepción, según tu lógica
   ```

### Caso 2: "Cambia la documentación Swagger de este endpoint"
1. Ve al controlador (ej. `CarreraController.java`).
2. Ubica la anotación `@Operation`.
3. Modifica el texto en `summary` o `description`:
   ```java
   @Operation(summary = "Obtiene carreras actualizadas", description = "Nueva descripción agregada en la defensa")
   ```
4. Reinicia el microservicio desde el IDE o con Docker para que refleje el cambio.

### Caso 3: "Añade un nuevo link HATEOAS a este modelo"
1. Ve a tu `Assembler` (ej. `CarreraModelAssembler.java`).
2. En el método `toModel`, añade un nuevo `linkTo`:
   ```java
   return EntityModel.of(carrera,
       linkTo(methodOn(CarreraControllerV2.class).getCarreraByCodigo(carrera.getCodigo())).withSelfRel(),
       linkTo(methodOn(CarreraControllerV2.class).getAllCarreras()).withRel("carreras"),
       // NUEVO LINK AÑADIDO:
       linkTo(methodOn(CarreraControllerV2.class).getCarreraByCodigo(carrera.getCodigo())).withRel("modificar_carrera")
   );
   ```

### Caso 4: "Cambia una ruta o pre-fijo en el Gateway"
1. Ve al `GatewayConfig.java` (o al archivo YAML si lo tienes por properties).
2. Ubica la configuración de rutas, por ejemplo `.path("/gateway/estudiantes/**")`.
3. Cámbialo por `.path("/api/v1/estudiantes/**")` y reinicia el servicio Gateway.

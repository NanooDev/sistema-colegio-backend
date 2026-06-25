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

### ☁️ 3.4 Docker y Despliegue Remoto (PaaS)
- **Docker:** Plataforma de contenedores que empaqueta el microservicio junto con sus dependencias para que corra igual en cualquier entorno.
  - *Conceptos:* **Dockerfile** (receta de la imagen), **Contenedor** (instancia en ejecución), **Docker Compose** (orquestador de múltiples servicios, el cual usamos para levantar todo el colegio).
  - *Buenas Prácticas:* Usar imágenes base ligeras (como Alpine), no incluir secretos en el Dockerfile, inyectar el puerto dinámicamente.
- **PaaS (Platform as a Service):** Plataformas en la nube como **Render, Railway o AlwaysData** que nos dan la infraestructura lista. Solo subimos el código o contenedor y la plataforma se encarga del servidor.
  - *Requisitos para desplegar:* El microservicio debe recibir el puerto por variable de entorno (ej. `${PORT}` de Railway), usar configuración externalizada (YAML) para separar el perfil de desarrollo local (`dev`) del remoto (`remote`).

---

## ⚡ 3. Guía de Cambios Rápidos (Posibles Preguntas del Profesor)

Si el profesor te pide realizar uno de los siguientes cambios en vivo para demostrar dominio del proyecto, aquí tienes exactamente cómo hacerlo paso a paso:

### 1. Modificar los datos globales de la API en Swagger
**Objetivo:** Cambiar el título, versión o descripción de la API y que se refleje en la URL de Swagger.
1. Ve a la **clase principal** del microservicio (ej. `ServicioEstudiantesApplication.java`).
2. Agrega o modifica la anotación `@OpenAPIDefinition` justo arriba de `@SpringBootApplication`.
   ```java
   @OpenAPIDefinition(
       info = @Info(
           title = "API de Estudiantes - Colegio XYZ",
           version = "2.5.0",
           description = "Gestión de matrículas y alumnos (Modificado en Defensa)"
       )
   )
   @SpringBootApplication
   public class ServicioEstudiantesApplication { ... }
   ```
3. Reinicia el servicio y refresca `http://localhost:<PUERTO>/doc/swagger-ui.html`. Verás el nuevo título y versión en el encabezado.

### 2. Agregar logs de tipo Info, Warn y Error
**Objetivo:** Registrar eventos importantes en la consola y archivo de texto.
1. Ve a un **Servicio** o **Controlador** (ej. `EstudianteService.java`).
2. Asegúrate de que la clase tenga la anotación `@Slf4j` de Lombok arriba del nombre de la clase.
3. Dentro de algún método (ej. `crearEstudiante()`), agrega los logs:
   ```java
   log.info("Iniciando la creación del estudiante con rut: {}", estudiante.getRut());
   
   if(estudiante.getEdad() < 4) {
       log.warn("El estudiante ingresado es inusualmente joven ({} años)", estudiante.getEdad());
   }
   
   try {
       // Lógica de guardado...
   } catch (Exception e) {
       log.error("Error crítico al guardar en la base de datos: {}", e.getMessage());
   }
   ```

### 3. Borrar un microservicio y su BD por completo de Docker
**Objetivo:** Eliminar todo rastro de un servicio y volver a levantar el orquestador.
1. Abre el archivo `docker-compose.yml`.
2. **Borra el bloque completo** del microservicio (ej. `servicio-asistencias: ...`).
3. **Borra el bloque completo** de su base de datos (ej. `mysql-servicio-asistencias: ...`).
4. En la terminal, bota los contenedores actuales eliminando huérfanos:
   ```bash
   docker compose down --remove-orphans
   ```
5. Elimina físicamente la carpeta del microservicio (opcional, si el profe lo pide).
6. Vuelve a levantar la arquitectura: `docker compose up -d --build`.

### 4. Agregar una nueva Base de Datos en el docker-compose
**Objetivo:** Sumar un nuevo contenedor de MySQL para un futuro microservicio.
1. Abre el archivo `docker-compose.yml`.
2. En la sección de `services:` (bajo las bases de datos), copia un bloque existente y cámbiale los nombres y el puerto expuesto.
   ```yaml
   mysql-servicio-nuevo:
     image: mysql:8.0
     container_name: mysql-servicio-nuevo
     environment:
       - MYSQL_ROOT_PASSWORD=root
     ports:
       - "3325:3306" # PUERTO ÚNICO EN LA MÁQUINA HOST
     networks:
       - microservicios-net
     healthcheck:
       test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
       interval: 10s
       timeout: 5s
       retries: 5
       start_period: 30s
   ```
3. Ejecuta `docker compose up -d` para que se cree y levante el nuevo contenedor.

### 5. Agregar un nuevo servicio en el API Gateway
**Objetivo:** Enrutar el tráfico de una nueva URL hacia un nuevo microservicio.
1. Abre `api-gateway/src/main/resources/application.yml`.
2. En la lista de `routes:` de `spring.cloud.gateway`, agrega un nuevo elemento:
   ```yaml
             - id: ruta-nuevo-servicio
               uri: lb://servicio-nuevo # Nombre exacto con el que se registrará en Eureka
               predicates:
                 - Path=/api/v1/nuevo/**
   ```
3. Reinicia el API Gateway. Ahora todas las peticiones a `http://localhost:8090/api/v1/nuevo/...` irán al nuevo servicio.

### 6. Modificar el puerto de salida del API Gateway
**Objetivo:** Cambiar el puerto donde atiende el Gateway (8090) por otro (ej. 8099) y verificar que Eureka siga funcionando.
1. Abre `api-gateway/src/main/resources/application.yml`.
2. Modifica la propiedad de puerto:
   ```yaml
   server:
     port: 8099
   ```
3. Si usas Docker, abre `docker-compose.yml`, busca el `api-gateway` y cambia el mapeo de puertos:
   ```yaml
     ports:
       - "8099:8099"
   ```
4. Reinicia el Gateway (o el contenedor). El Gateway seguirá consultando correctamente a Eureka (porque tiene `http://eureka:8761/eureka/` configurado) y los clientes ahora deberán usar el puerto `8099`.

### 7. Agregar una prueba unitaria de un "caso límite" (Edge Case)
**Objetivo:** Probar qué pasa cuando una función recibe un ID que no existe y confirmar que arroje error o retorne nulo.
1. Ve a la carpeta `src/test/java/.../service/` de algún microservicio (ej. `EstudianteServiceTest.java`).
2. Agrega el siguiente método usando Mockito para forzar un escenario de error:
   ```java
   @Test
   @DisplayName("Debe lanzar excepción cuando el Estudiante no existe")
   void testFindById_NoExiste() {
       // GIVEN (Dado que la base de datos no tiene este ID)
       Long idInexistente = 999L;
       when(estudianteRepository.findById(idInexistente)).thenReturn(Optional.empty());

       // WHEN & THEN (Cuando lo busco, Entonces espero una Excepción)
       assertThrows(RuntimeException.class, () -> {
           estudianteService.findById(idInexistente);
       });
       
       // Verificar que el repositorio efectivamente fue llamado 1 vez
       verify(estudianteRepository, times(1)).findById(idInexistente);
   }
   ```

# Sistema de Gestion Escolar - Backend

Arquitectura de microservicios para la gestion de un colegio, desarrollada con **Java 21**, **Spring Boot 4.0.5**, **Spring Cloud 2025.1.1** y **MySQL 8.0** gestionado en **Docker**.

## Stack Tecnologico

| Tecnologia | Version | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 4.0.5 | Framework de cada microservicio |
| Spring Cloud | 2025.1.1 | Eureka (discovery) + Gateway (enrutamiento) + OpenFeign (comunicacion entre servicios) |
| MySQL | 8.0 | Base de datos (una por microservicio, dockerizada) |
| Liquibase | - | Migraciones y versionado del esquema de BD |
| Springdoc OpenAPI | 3.0.3 | Documentacion Swagger de cada API |
| Maven Wrapper | - | Construccion del proyecto (no requiere Maven instalado) |
| Docker Compose | 3.8 | Orquestacion de contenedores |

---

## Arquitectura

```
                         [API Gateway :8090]
                                |
                         [Eureka :8761]
                                |
    ----------------------------------------------------------------
    |           |           |          |          |                 |
[Estudiantes] [Profesores] [Cursos] [Asignaturas] [Calificaciones] ...
   :8081        :8082       :8084     :8085         :8095
```

Cada microservicio tiene su propia base de datos MySQL, se registra en Eureka y es accesible a traves del API Gateway.

---

## Microservicios y Puertos

| Servicio | Puerto App | Puerto MySQL (Docker) | Base de datos |
|---|---|---|---|
| Eureka Server | 8761 | - | - |
| API Gateway | 8090 | - | - |
| servicio-estudiantes | 8081 | 3311 | db_estudiantes |
| servicio-profesores | 8082 | 3312 | db_profesores |
| servicio-asistencias | 8083 | 3313 | db_asistencias |
| servicio-cursos | 8084 | 3314 | db_cursos |
| servicio-asignaturas | 8085 | 3315 | db_asignaturas |
| servicio-matriculas | 8086 | 3316 | db_matriculas |
| servicio-biblioteca | 8087 | 3317 | db_biblioteca |
| servicio-finanzas | 8088 | 3318 | db_finanzas |
| servicio-notificaciones | 8089 | 3319 | db_notificaciones |
| servicio-calificaciones | 8095 | 3320 | db_calificaciones |

---

## Requisitos Previos

Antes de levantar el sistema necesitas tener instalado:

1. **Java 21 (JDK)** - Verificar con `java -version`
2. **Docker Desktop** (Windows/Mac) o **Docker Engine + docker-compose** (Linux)
   - En Windows: abrir Docker Desktop y verificar que el motor este corriendo (icono verde)
3. **Git** - Para clonar el repositorio

> No necesitas instalar Maven. El proyecto incluye Maven Wrapper (`mvnw` / `mvnw.cmd`) que descarga automaticamente la version correcta.

---

## Guia de Instalacion y Ejecucion

### Opcion A: Levantar TODO con Docker Compose (Recomendado)

Este comando construye y levanta los 10 microservicios, Eureka, el Gateway y las 10 bases de datos MySQL:

```bash
# 1. Clonar el repositorio
git clone https://github.com/NanooDev/sistema-colegio-backend.git
cd sistema-colegio-backend

# 2. Levantar todo
docker compose up -d --build
```

Espera unos minutos a que todos los contenedores esten healthy. Puedes verificar el estado con:

```bash
docker ps
```

Una vez levantado:
- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:8090
- **Swagger del Gateway:** http://localhost:8090/doc/swagger-ui.html

> **Nota:** Requiere buena cantidad de RAM. Si tu equipo tiene menos de 8 GB, usa la Opcion B.

### Perfiles de configuracion

Cada servicio tiene dos perfiles en su `application.yml`:

| Perfil | Cuando se usa | BD | Eureka |
|---|---|---|---|
| base (sin perfil) | Desarrollo local (`mvnw spring-boot:run`) | `localhost:3311-3320` (las BD dockerizadas) | `localhost:8761` |
| `docker` | Dentro de contenedores (lo activa docker-compose via `SPRING_PROFILES_ACTIVE=docker`) | `mysql-servicio-X:3306` (red interna) | `eureka:8761` |

No se necesita configurar nada manualmente: docker-compose activa el perfil correcto y en local se usa el perfil base por defecto.

### Opcion B: Levantar servicios individualmente

Util para desarrollo o si quieres levantar solo los servicios que necesitas.

**Paso 1 - Levantar MySQL con Docker:**

Cada microservicio tiene su propia BD, expuesta en un puerto distinto del host (3311-3320, ver tabla de puertos). Los `application.yml` ya apuntan a esos puertos, asi que basta con levantar la BD que necesites:

```bash
docker compose up -d mysql-servicio-estudiantes
```

**Paso 2 - Levantar Eureka (obligatorio):**

Eureka es el service discovery. Todos los microservicios se registran ahi.

Windows:
```cmd
cd eureka
.\mvnw.cmd spring-boot:run
```

Linux / Mac:
```bash
cd eureka
./mvnw spring-boot:run
```

**Paso 3 - Levantar el microservicio que necesites:**

Windows:
```cmd
cd servicio-estudiantes
.\mvnw.cmd spring-boot:run
```

Linux / Mac:
```bash
cd servicio-estudiantes
./mvnw spring-boot:run
```

> Repetir para cada microservicio que necesites, cada uno en su propia terminal.

**Paso 4 - Levantar el API Gateway (opcional, necesario para acceder via puerto unico):**

Windows:
```cmd
cd api-gateway
.\mvnw.cmd spring-boot:run
```

Linux / Mac:
```bash
cd api-gateway
./mvnw spring-boot:run
```

---

## Documentacion Swagger

Cada microservicio expone su documentacion Swagger en:

```
http://localhost:{PUERTO}/doc/swagger-ui.html
```

Ejemplos:
- Estudiantes: http://localhost:8081/doc/swagger-ui.html
- Profesores: http://localhost:8082/doc/swagger-ui.html
- Gateway (agrupa todos): http://localhost:8090/doc/swagger-ui.html

El Swagger del Gateway federa la documentacion de los 10 microservicios: usa el **selector desplegable** (arriba a la derecha) para cambiar entre estudiantes, profesores, cursos, asignaturas, asistencias, calificaciones, matriculas, biblioteca, finanzas y notificaciones, y probar cualquier endpoint desde un unico punto.

La documentacion incluye ejemplos de request/response gracias a las anotaciones `@Schema` en los DTOs y `@Operation`/`@ApiResponses` en los controllers.

> **CORS:** el Gateway maneja CORS de forma global (`globalcors`), por lo que cualquier frontend puede consumir la API via `http://localhost:8090` sin configuracion adicional. Los microservicios no usan `@CrossOrigin`.

---

## Endpoints principales (via Gateway)

Todos los endpoints son accesibles a traves del Gateway en `http://localhost:8090`:

| Recurso | URL |
|---|---|
| Estudiantes | `http://localhost:8090/api/v1/estudiantes` |
| Profesores | `http://localhost:8090/api/v1/profesores` |
| Cursos | `http://localhost:8090/api/v1/cursos` |
| Asignaturas | `http://localhost:8090/api/v1/asignaturas` |
| Asistencias | `http://localhost:8090/api/v1/asistencias` |
| Calificaciones | `http://localhost:8090/api/v1/calificaciones` |
| Matriculas | `http://localhost:8090/api/v1/matriculas` |
| Biblioteca | `http://localhost:8090/api/v1/biblioteca` |
| Finanzas | `http://localhost:8090/api/v1/finanzas` |
| Notificaciones | `http://localhost:8090/api/v1/notificaciones` |

Cada recurso soporta las operaciones CRUD estandar: `GET`, `POST`, `PUT`, `DELETE`.

### Endpoints de detalle (comunicacion entre microservicios)

Estos endpoints enriquecen la respuesta con datos de otros microservicios via OpenFeign:

| Endpoint | Descripcion |
|---|---|
| `GET /api/v1/cursos/{id}` | Curso con profesor jefe y lista de estudiantes (Feign a profesores y estudiantes) |
| `GET /api/v1/matriculas/{id}/detalle` | Matricula con nombre del estudiante y nombre del curso |
| `GET /api/v1/asistencias/{id}/detalle` | Asistencia con nombre del estudiante |
| `GET /api/v1/calificaciones/{id}/detalle` | Calificacion con nombre del estudiante |
| `GET /api/v1/finanzas/{id}/detalle` | Cargo financiero con nombre del estudiante |

Los clientes Feign resuelven por nombre registrado en Eureka (con load balancing), por lo que funcionan igual en local y en Docker. Si el servicio remoto no responde, el endpoint degrada la respuesta (campos remotos en `null` o "Desconocido") en lugar de fallar.

### Datos de ejemplo (seeds)

Al levantar el sistema, Liquibase inserta datos iniciales coherentes entre servicios para poder probar los endpoints de detalle de inmediato:

| Servicio | Datos |
|---|---|
| Profesores | Maria Gonzalez (id 1), Pedro Rojas (id 2) |
| Cursos | 1 Basico A (id 1, profesor jefe 1), 2 Basico B (id 2, profesor jefe 2) |
| Estudiantes | Juan Perez (id 1) y Ana Soto (id 2), ambos en el curso 1 |
| Calificaciones | 3 notas que referencian a los estudiantes 1 y 2 en los cursos 1 y 2 |

Prueba rapida: `GET http://localhost:8090/api/v1/cursos/1` devuelve el curso enriquecido con su profesora jefe y sus dos estudiantes.

---

## Logs

Cada microservicio genera logs en la carpeta `logs/` de su directorio:
- `logs/{nombre-servicio}.log` - Log de aplicacion
- `logs/{nombre-servicio}-access.log` - Log de acceso HTTP (requests, status, tiempos)

---

## Testing

El proyecto cuenta con dos capas de testing:

### Tests unitarios de Service (`@ExtendWith(MockitoExtension.class)`)

Validan la logica de negocio de cada microservicio con mocks del repositorio y clientes Feign. Estructura **Given-When-Then** con asserts precisos.

Cobertura por servicio:
- CRUD completo: guardar, listar, buscarPorId, actualizar, eliminar
- Reglas de negocio: validacion de RUT duplicado (estudiantes), calculo de nota final y estado aprobado/reprobado (calificaciones)
- Excepciones: `*NotFoundException` en operaciones sobre entidades inexistentes
- Comunicacion REST: exito y fallback cuando el servicio remoto no responde

### Tests de Controller (`@WebMvcTest` + `@MockitoBean` + `MockMvc`)

Validan los endpoints REST con Spring context, verificando codigos HTTP, serializacion JSON y comportamiento del controller.

### Ejecutar tests

```bash
cd servicio-estudiantes
./mvnw test
```

Para ejecutar los tests de todos los servicios:

```bash
for dir in servicio-*/; do echo "=== $dir ===" && cd "$dir" && ./mvnw test -q && cd ..; done
```

---

## Flujo de Trabajo Git

```bash
# 1. Clonar (solo la primera vez)
git clone https://github.com/NanooDev/sistema-colegio-backend.git

# 2. Actualizarse antes de trabajar
git pull origin main

# 3. Crear rama para tu funcionalidad
git checkout -b funcionalidad/nombre-descriptivo

# 4. Guardar y subir cambios
git add .
git commit -m "Descripcion clara del cambio"
git push origin funcionalidad/nombre-descriptivo

# 5. Crear Pull Request en GitHub
```

---

## Organizacion del Equipo

- **Servicio Estudiantes**: Mariano
- **Servicio Profesores**: Alvaro
- **Servicio Cursos**: Felipe

---

## Historial de Cambios

### 2026-07-10 - Alineacion con arquitectura de referencia y correcciones de despliegue (Felipe Sepulveda)

1. **Feign via Eureka:** Se elimino el atributo `url` de los 7 `@FeignClient` (apuntaba a `localhost`, lo que rompia la comunicacion inter-servicio dentro de Docker). Ahora resuelven por nombre registrado en Eureka con `path` para el prefijo `/api/v1/...`, con load balancing incluido. Se elimino la configuracion `feign.*.url` de los application.yml.

2. **CORS centralizado en el Gateway:** Se agrego `globalcors` en el API Gateway (origenes, metodos y headers abiertos). Ningun microservicio necesita `@CrossOrigin`.

3. **Swagger agregado en el Gateway:** Se agregaron 10 rutas `/v3/api-docs/{servicio}` con `RewritePath` y el selector `springdoc.swagger-ui.urls`, para navegar la documentacion de los 10 microservicios desde `http://localhost:8090/doc/swagger-ui.html`.

4. **Perfil `docker` funcional:** Se corrigieron los hostnames de BD del perfil docker (`mysql-X` -> `mysql-servicio-X`) en los 10 servicios, se agrego perfil docker al Gateway, y docker-compose ahora activa `SPRING_PROFILES_ACTIVE=docker` en lugar de duplicar la configuracion en variables de entorno (se eliminaron las env vars redundantes y las `SERVICIO_*_URL` sin uso).

5. **Datos de ejemplo coherentes:** Se agregaron seeds Liquibase para profesores, cursos y estudiantes, alineados con los IDs que referencian los seeds de calificaciones, para que los endpoints `/detalle` funcionen apenas levanta el sistema.

6. **Dockerfiles con Maven Wrapper:** Se completo la migracion declarada anteriormente: los 12 Dockerfiles ahora usan `./mvnw` en lugar de instalar Maven via `apk`, conservando el mirror de Google.

7. **Limpieza:** Logging del Gateway de `TRACE` a `info`, logging con SLF4J en `CursoService` (antes `System.err.println`), y eliminacion de archivos `fix*.patch` residuales.

8. **Desarrollo local funcional (Opcion B):** Los `application.yml` base ahora apuntan a los puertos que docker-compose expone para cada BD (`localhost:3311-3320`); antes apuntaban a `localhost:3306`, por lo que el flujo documentado de correr un servicio local contra su BD dockerizada no conectaba.

9. **Documentacion:** Se corrigio el endpoint de detalle de cursos (`GET /api/v1/cursos/{id}`, no `/{id}/detalle`), y se documentaron el selector de Swagger federado, el CORS global del Gateway, los perfiles base/docker y los datos de ejemplo (seeds).

### 2026-06-26 - Integracion de pruebas unitarias, YAML y comunicacion inter-servicio (Felipe Sepulveda)

Mejoras para cumplir con los requisitos de la Evaluacion Parcial 3:

1. **Pruebas unitarias de capa Service:** Se crearon 10 clases `*ServiceTest` con estructura Given-When-Then, `@ExtendWith(MockitoExtension.class)`, `@Mock` para repositorios y clientes Feign, y `@InjectMocks`. Cobertura de CRUD completo, reglas de negocio (RUT duplicado, calculo de nota final, estado aprobado/reprobado) y manejo de errores.

2. **Migracion a YAML:** Se reemplazaron los 10 archivos `application.properties` por `application.yml` con configuracion organizada por secciones y perfil `docker` separado para despliegue en contenedores.

3. **Comunicacion REST inter-servicio:** Se agrego OpenFeign a 4 servicios adicionales (matriculas, asistencias, calificaciones, finanzas) para consumir datos de servicio-estudiantes y servicio-cursos. Cada servicio incluye endpoint `/detalle` con manejo de errores remotos y logging.

4. **`@EnableFeignClients`:** Se agrego la anotacion en las Application classes de matriculas, asistencias, calificaciones y finanzas.

5. **DTOs enriquecidos:** Se agregaron campos `nombreEstudiante` y `nombreCurso` a los DTOs de matriculas, asistencias, calificaciones y finanzas con anotaciones `@Schema` para Swagger.

### 2026-06-26 - Correccion de diferencias respecto a la referencia de clases (Felipe Sepulveda)

Correcciones aplicadas segun el informe de diferencias entregado por el profesor:

1. **Patron de servicio (servicio-cursos):** Se elimino el patron Interface + Implementacion (`CursoService` interface + `CursoServiceImpl`) y se reemplazo por clase directa `@Service`, consistente con los demas microservicios y lo visto en clases.

2. **Anotaciones Swagger en Controllers:** Se agregaron `@Tag`, `@Operation`, `@ApiResponses` y `@ApiResponse` en los 10 controllers para documentar cada endpoint en Swagger UI.

3. **Anotaciones @Schema en DTOs:** Se agregaron `@Schema(description, example)` en todos los campos de los 21 DTOs y Requests para que Swagger muestre ejemplos concretos en la documentacion.

4. **Patron de Testing:** Se migraron los 10 tests del patron `@ExtendWith(MockitoExtension.class)` + `@Mock` + `@InjectMocks` (unit test del service) al patron `@WebMvcTest` + `@MockitoBean` + `MockMvc` (test de integracion del controller), como se vio en clases. Se agrego la dependencia `spring-boot-starter-webmvc-test` requerida en Spring Boot 4.x.

5. **Dockerfiles:** Se reemplazo la instalacion de Maven via `apk` con mirror de Google por el Maven Wrapper (`mvnw`) incluido en el proyecto, en los 12 Dockerfiles.

6. **Nombres en Gateway:** Se cambiaron las URIs del API Gateway de minusculas (`lb://servicio-estudiantes`) a MAYUSCULAS (`lb://SERVICIO-ESTUDIANTES`), siguiendo la convencion de Eureka.

7. **InfoController:** Se eliminaron los 6 InfoControllers que no eran parte de los requerimientos del proyecto (asignaturas, asistencias, biblioteca, finanzas, matriculas, notificaciones).

8. **Bug servicio-calificaciones:** Se corrigio el conflicto Flyway/Liquibase que impedia levantar el microservicio. El `pom.xml` tenia la dependencia de Liquibase pero el `application.properties` configuraba Flyway. Se migro a Liquibase para ser consistente con los demas servicios.

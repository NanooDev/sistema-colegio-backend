# Sistema Colegio Backend

Este proyecto consiste en un sistema de microservicios desarrollado con Spring Boot y MySQL. A continuación, se detallan los pasos para configurar y ejecutar el proyecto en un entorno Windows.

## Requisitos previos

1. **Instalar Java 21**:
   - Descarga e instala Java 21 desde [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) o [OpenJDK](https://openjdk.org/).
   - Verifica la instalación ejecutando:
     ```bash
     java -version
     ```

2. **Instalar Maven**:
   - Descarga e instala Maven desde [Maven](https://maven.apache.org/download.cgi).
   - Verifica la instalación ejecutando:
     ```bash
     mvn -version
     ```

3. **Instalar Docker**:
   - Descarga e instala Docker Desktop desde [Docker](https://www.docker.com/products/docker-desktop/).
   - Verifica la instalación ejecutando:
     ```bash
     docker --version
     ```

## Configuración del proyecto

1. **Clonar el repositorio**:
   - Asegúrate de tener Git instalado y clona el repositorio:
     ```bash
     git clone <URL_DEL_REPOSITORIO>
     ```

2. **Configurar la base de datos**:
   - Navega al directorio `servicio-estudiantes` y ejecuta el siguiente comando para iniciar la base de datos:
     ```bash
     docker-compose up -d
     ```
   - Esto iniciará un contenedor con MySQL en el puerto `3306`.

3. **Compilar los microservicios**:
   - Navega a cada microservicio y ejecuta:
     ```bash
     ./mvnw clean install
     ```

4. **Ejecutar los microservicios**:
   - Para iniciar un microservicio, navega a su directorio y ejecuta:
     ```bash
     ./mvnw spring-boot:run
     ```

## Estructura del proyecto

- **servicio-asignaturas**: Gestión de asignaturas.
- **servicio-asistencias**: Registro de asistencias.
- **servicio-biblioteca**: Gestión de préstamos de libros.
- **servicio-calificaciones**: Registro de calificaciones.
- **servicio-cursos**: Gestión de cursos.
- **servicio-estudiantes**: Gestión de estudiantes.
- **servicio-finanzas**: Gestión financiera.
- **servicio-matriculas**: Gestión de matrículas.
- **servicio-notificaciones**: Envío de notificaciones.
- **servicio-profesores**: Gestión de profesores.

## Conexión entre microservicios

Los microservicios se comunican entre sí utilizando REST APIs. Cada uno tiene su propio puerto y se conecta a la base de datos MySQL configurada en `docker-compose.yml`.

## Migraciones

Las migraciones de base de datos se gestionan con Liquibase. Para ejecutar una migración:
1. Asegúrate de que la base de datos esté en ejecución.
2. Ejecuta el siguiente comando en el microservicio correspondiente:
   ```bash
   ./mvnw liquibase:update
   ```

## Crear un nuevo endpoint

1. Abre el archivo correspondiente en `src/main/java`.
2. Crea un nuevo controlador o método en el controlador existente.
3. Asegúrate de agregar las anotaciones necesarias (`@GetMapping`, `@PostMapping`, etc.).
4. Compila y ejecuta el microservicio para probar el nuevo endpoint.

## Conectar un nuevo microservicio

1. Agrega la dependencia de OpenFeign en el archivo `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```
2. Configura el cliente Feign en el microservicio.
3. Asegúrate de que el microservicio esté registrado en el servidor de configuración (si aplica).

## Preguntas frecuentes

1. **¿Cómo reinicio la base de datos?**
   - Detén el contenedor:
     ```bash
     docker-compose down
     ```
   - Elimina los datos persistentes:
     ```bash
     rm -rf datos_mysql
     ```
   - Reinicia el contenedor:
     ```bash
     docker-compose up -d
     ```

2. **¿Cómo verifico que todo está funcionando?**
   - Asegúrate de que todos los microservicios estén en ejecución.
   - Prueba los endpoints utilizando herramientas como Postman o cURL.

---

¡Buena suerte en tu prueba!
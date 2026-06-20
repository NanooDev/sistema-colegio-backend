# Sistema Colegio Backend - Evaluación 3 (EV3)

Este documento detalla todas las integraciones, correcciones y mejoras aplicadas en el proyecto para asegurar que la arquitectura de microservicios funcione correctamente de manera local a través de Docker Compose, incluyendo instrucciones detalladas para su levantamiento tanto en Linux como en Windows.

## 🚀 Mejoras e Integraciones Realizadas

1. **Refactorización del API Gateway:**
   - Se migró la configuración de rutas de YAML (`application.yml`) a una **configuración programática en Java** (`GatewayConfig.java`). Esto soluciona problemas de compatibilidad y parseo en las nuevas versiones de Spring Cloud Gateway WebFlux, asegurando que los predicados de ruta y el filtro `StripPrefix=2` se apliquen correctamente.
   - Se añadió correctamente el cliente de Eureka (`spring-cloud-starter-netflix-eureka-client`) al Gateway para que se registre en el panel centralizadoizado.

2. **Sincronización con Eureka:**
   - Se corrigió la propiedad de conexión a Eureka en los 10 microservicios. Se encontraba escrita erróneamente con guión (`eureka.client-service-url.defaultZone`), lo que ocasionaba que los servicios intentaran conectarse a su propio `localhost`. Se corrigió por la propiedad oficial de Spring: `eureka.client.service-url.defaultZone`.

3. **Resolución de problemas con Flyway y JPA en `servicio-calificaciones`:**
   - Hibernate intentaba validar las entidades de base de datos antes de que Flyway tuviera la oportunidad de crear el esquema. Se corrigió desactivando la validación automática de DDL de Hibernate (`spring.jpa.hibernate.ddl-auto=none`), dándole control total a Flyway.
   - Se unificó el nombre de la base de datos a `db_calificaciones` (estaba mal escrito en algunas partes como `db_califcaciones`).

4. **Soporte robusto para MySQL en Docker (Linux y Windows):**
   - Se implementó la configuración `--tmpfs` (en `docker-compose.yml`) para evadir los problemas de permisos al montar volúmenes de MySQL en distribuciones Linux recientes (como Arch Linux / CachyOS).

5. **Corrección de dependencias:**
   - Se eliminaron las dependencias conflictivas de tests excluyendo `junit-vintage-engine` donde correspondía y corrigiendo las versiones en los `pom.xml` de todo el proyecto.

---

## 🛠️ Cómo levantar el proyecto

Este repositorio está orquestado para ser levantado en un solo comando mediante `docker-compose`. 

### Prerrequisitos
- Tener **Docker Desktop** instalado (en Windows) o **Docker Daemon + Docker Compose** (en Linux).
- Tener los puertos libres: `3307` (MySQL), `8761` (Eureka) y `8090` (API Gateway).

### Instrucciones para Linux y Windows

1. **Abre una terminal** en la raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`).
2. **(Opcional pero recomendado)** Limpia cualquier contenedor previo o base de datos corrupta:
   ```bash
   docker compose down -v
   ```
3. **Construye e inicia todos los servicios:**
   ```bash
   docker compose up --build -d
   ```
   > **Nota:** El flag `--build` es crítico la primera vez o cada vez que hagas un cambio en el código fuente, ya que fuerza a Docker a compilar todos los `.jar` e instalar los últimos cambios.

### ¿Qué sucede al ejecutar este comando?
- **Base de datos:** Se levantará un contenedor de MySQL llamado `db-productos` en el puerto `3307`.
- **Eureka Server:** Iniciará en el puerto `8761`.
- **Microservicios y Gateway:** Docker construirá progresivamente los 10 microservicios (Estudiantes, Profesores, Asistencias, Cursos, Asignaturas, Matrículas, Biblioteca, Finanzas, Notificaciones, Calificaciones) y el `api-gateway` (Puerto `8090`). Esto puede tardar unos minutos dependiendo de la conexión a internet y el CPU.

### Verificación
1. Ingresa a [http://localhost:8761](http://localhost:8761) en tu navegador.
2. Después de 1 o 2 minutos, deberás ver **11 instancias registradas** con estado `UP` (API-GATEWAY + los 10 servicios).
3. Prueba cualquier endpoint a través del gateway, por ejemplo:
   ```
   http://localhost:8090/gateway/estudiantes/api/v1/estudiantes
   ```

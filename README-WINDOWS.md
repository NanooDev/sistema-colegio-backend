# Sistema Colegio Backend - Guia rapida para Windows

Notas especificas para ejecutar el proyecto en Windows. La guia completa (arquitectura, puertos, endpoints, testing) esta en el [README principal](README.md).

## Requisitos previos

1. **Java 21 (JDK)** - Descargar de [Adoptium](https://adoptium.net/) u [OpenJDK](https://openjdk.org/). Verificar con:
   ```cmd
   java -version
   ```
2. **Docker Desktop** - Descargar de [Docker](https://www.docker.com/products/docker-desktop/). Abrirlo y verificar que el motor este corriendo (icono verde). Verificar con:
   ```cmd
   docker --version
   ```
3. **Git** - Para clonar el repositorio.

> **No necesitas instalar Maven.** Cada modulo incluye Maven Wrapper: en Windows usa `mvnw.cmd` (cmd/PowerShell) o `./mvnw` (Git Bash).

## Opcion A: Levantar todo con Docker (recomendado)

Desde la **raiz del proyecto**:

```cmd
docker compose up -d --build
```

Esto construye y levanta los 10 microservicios, Eureka, el API Gateway y las 10 bases de datos MySQL. La primera vez tarda varios minutos.

Verificar:
- Eureka Dashboard: http://localhost:8761
- Swagger (todos los servicios): http://localhost:8090/doc/swagger-ui.html

## Opcion B: Ejecutar un microservicio localmente

1. Levantar la BD del servicio (desde la raiz):
   ```cmd
   docker compose up -d mysql-servicio-estudiantes
   ```
   Cada BD queda expuesta en su propio puerto del host (3311-3320, ver tabla del README principal). Los `application.yml` ya apuntan a esos puertos.

2. Levantar Eureka:
   ```cmd
   cd eureka
   mvnw.cmd spring-boot:run
   ```

3. Levantar el microservicio (en otra terminal):
   ```cmd
   cd servicio-estudiantes
   mvnw.cmd spring-boot:run
   ```

## Ejecutar tests

```cmd
cd servicio-estudiantes
mvnw.cmd test
```

Para todas las suites (Git Bash):
```bash
bash scripts/run_all_tests.sh
```

## Preguntas frecuentes

1. **¿Como reinicio las bases de datos desde cero?**
   ```cmd
   docker compose down
   docker compose up -d --build
   ```
   Las BD no usan volumenes, asi que al eliminar los contenedores los datos se regeneran con las migraciones y seeds de Liquibase.

2. **¿Como verifico que todo esta funcionando?**
   - Abre http://localhost:8761 y confirma que aparecen los 10 servicios y el gateway.
   - Prueba un endpoint enriquecido: http://localhost:8090/api/v1/cursos/1

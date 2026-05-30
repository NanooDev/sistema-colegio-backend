# 🏫 Sistema de Gestión Escolar - Backend

Este proyecto consiste en una arquitectura de microservicios para la gestión de un colegio, desarrollada con **Java 21**, **Spring Boot** y **MySQL** gestionado en **Docker**.

## 🛠️ Stack Tecnológico
* **Lenguaje:** Java 21
* **Framework:** Spring Boot
* **Base de Datos:** MySQL 8.0 (Dockerizado)
* **Construcción:** Maven (a través de `mvnw` y `mvnw.cmd`)

---

## 🚀 Guía de Instalación y Ejecución

Para poder echar a correr el sistema (la base de datos y los microservicios), sigue estos pasos cuidadosamente dependiendo de tu Sistema Operativo:

### 1. Requisitos Previos
* **Java 21**: Necesitas tener el JDK 21 instalado en tu ordenador.
* **Docker**: 
  * **En Windows:** Instalar [Docker Desktop](https://www.docker.com/products/docker-desktop/). IMPORTANTE: Ábrelo y ve que el motor de Docker esté corriendo (icono verde) antes de seguir.
  * **En Linux:** Tener instalado `docker` y el plugin `docker-compose`.
* (Opcional) **IntelliJ IDEA**, **Eclipse** o **VS Code** con extensiones para Java.

### 2. Levantar la Base de Datos MySQL (Docker)
Antes de ejecutar cualquier microservicio, la base de datos debe estar corriendo.

**Usuarios de Windows (CMD o PowerShell):**
1. Abre tu terminal (Símbolo del Sistema o PowerShell).
2. Navega a la carpeta que contiene el Docker Compose y levanta el contenedor:
   ```cmd
   cd servicio-estudiantes
   docker compose up -d
   cd ..
   ```

**Usuarios de Linux / Mac:**
1. Abre una terminal.
2. Levanta el contenedor:
   ```bash
   cd servicio-estudiantes
   docker compose up -d
   cd ..
   ```

*(Nota: Esto levantará el contenedor de MySQL en el puerto 3306 usando como contraseña root `root`. Los datos persistirán localmente en las carpetas `datos_mysql`).*

### 3. Ejecutar los Microservicios
El sistema está compuesto por múltiples servicios independientes. Debes levantar los que necesites probar. 

Dado que usamos el _wrapper_ de Maven (`mvnw`), **no necesitas instalar Maven en tu PC**. 

Para arrancar un servicio (por ejemplo, el de Estudiantes), abre una nueva terminal y ejecuta:

**Usuarios de Windows (CMD o PowerShell):**
```cmd
cd servicio-estudiantes
.\mvnw.cmd spring-boot:run
```

**Usuarios de Linux / Mac:**
```bash
cd servicio-estudiantes
./mvnw spring-boot:run
```

>**IMPORTANTE**: Debes repetir este paso para cada microservicio que desees levantar (ej. `servicio-profesores`, `servicio-cursos`, etc.) en *terminales distintas*. Asegúrate de que cada servicio esté configurado (en su `application.properties`/`yml`) para correr en **puertos diferentes** (ej: 8081, 8082...), de lo contrario, chocarán y dará error.

### 3.1 Ejecutar API Gateway (requerido para evaluación 3)
Se agregó un gateway en `api-gateway` para centralizar el enrutamiento de los microservicios.

**Windows:**
```cmd
cd api-gateway
..\servicio-estudiantes\mvnw.cmd spring-boot:run
```

**Linux / Mac:**
```bash
cd api-gateway
../servicio-estudiantes/mvnw spring-boot:run
```

Gateway URL base: `http://localhost:8090`

Ejemplo de consumo por gateway:
- `http://localhost:8090/gateway/estudiantes/api/v1/estudiantes`
- `http://localhost:8090/gateway/profesores/api/profesores`

Swagger del gateway:
- `http://localhost:8090/doc/swagger-ui.html`

### 3.2 Logs de aplicación y acceso HTTP
Cada microservicio genera:
- Log de aplicación en `logs/<spring.application.name>.log`
- Log de acceso HTTP en `logs/<spring.application.name>-access.log`

Esto deja evidencia trazable para defensa técnica (errores, requests, códigos HTTP y tiempos de respuesta).

### 4. Configuración de Base de Datos y Creación de Tablas
El sistema debería crear automáticamente las tablas iniciales al arrancar cada microservicio (gracias a Hibernate/JPA o Liquibase). NO necesitas crear bases de datos ni tablas manualmente.

---

## 📐 Flujo de Trabajo del Equipo (Git)

Los comandos de Git son exactamente **los mismos** para Windows y Linux:

### Paso 1: Clonar el proyecto (Solo la primera vez)
```bash
git clone https://github.com/NanooDev/sistema-colegio-backend.git
```

### Paso 2: Actualizarse antes de empezar a trabajar
**REGLA DE ORO**: Antes de escribir una sola línea de código, deben traer lo que sus compañeros hicieron para evitar conflictos:
```bash
git pull origin main
```

### Paso 3: Trabajar en una "Rama" (Branch)
Nunca trabajen directo en `main`. Creen su propio espacio:
```bash
# Ejemplo creando rama para el servicio de estudiantes
git checkout -b funcionalidad/estudiantes
```

### Paso 4: Guardar y subir sus cambios
```bash
git add .
git commit -m "Descripción clara del cambio"
git push origin funcionalidad/estudiantes
```

### Paso 5: Unir los cambios (Pull Request)
En GitHub, usa el botón **"Compare & pull request"**. Un encargado debe revisar y dar "Merge".

---
## 👥 Organización de Servicios (Referencia)
- **Servicio Estudiantes**: Mariano 
- **Servicio Profesores**: Alvaro
- **Servicio Cursos**: Felipe
- (Otros servicios en desarrollo: *Asignaturas, Asistencias, Biblioteca, Calificaciones, Finanzas, Matriculas, Notificaciones*).
- **API Gateway**: `api-gateway` (puerto 8090)

## 🧪 Pruebas con Postman
Si deseas probar la comunicación real entre los microservicios, consulta: [Guía de Pruebas de API (TESTING.md)](TESTING.md).

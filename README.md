🚩 Paso 1: Clonar el proyecto (Solo la primera vez)

Mariano creará el repositorio en la página de GitHub. Una vez creado, les pasará un link. Ustedes abren una terminal y escriben:

git clone https://github.com/usuario/sistema-colegio-backend.git

(Esto descarga una copia de todo el proyecto en su PC).

🔄 Paso 2: Actualizarse antes de empezar a trabajar

REGLA DE ORO: Antes de escribir una sola línea de código, deben traer lo que sus compañeros hicieron para no tener conflictos:

git pull origin main

🌿 Paso 3: Trabajar en una "Rama" (Branch)

Nunca trabajen directo en main. Creen su propio espacio:
Bash

# Ejemplo para Mariano creando el servicio de estudiantes
git checkout -b funcionalidad/estudiantes

💾 Paso 4: Guardar y subir sus cambios

Cuando terminen una parte del código:

    Guardar localmente: git add . (esto selecciona todo lo que cambiaron).

    Ponerle un nombre: git commit -m "Cree la entidad Estudiante y su base de datos".

    Subir a la nube: git push origin funcionalidad/estudiantes.

🤝 Paso 5: Unir los cambios (Pull Request)

En la página de GitHub, aparecerá un botón verde que dice "Compare & pull request". Mariano debe revisarlo y darle a "Merge" para que ese código pase a la rama principal del colegio.

# 🏫 Sistema de Gestión Escolar - Backend

Este proyecto consiste en una arquitectura de microservicios para la gestión de un colegio, desarrollada con **Java 21**, **Spring Boot 4.0.5** y **Docker**.

## 🛠️ Stack Tecnológico
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 4.0.5
* **Base de Datos:** MySQL 8.0 (Dockerizado)
* **Migraciones:** Liquibase (Gestión de versiones de BD)
* **Contenedores:** Docker Compose V2
Esto levantará el contenedor de MySQL en el puerto 3306.

3. Configuración de Base de Datos

Cada microservicio usa Liquibase. No crees tablas manualmente. Al arrancar el servicio por primera vez, las tablas se crearán solas gracias a los archivos en src/main/resources/db/changelog/.
📦 Microservicios y Responsabilidades
Servicio	Encargado	Estado
servicio-estudiantes	Mariano Acevedo	✅ Base Lista
servicio-profesores	Alvaro	⏳ Pendiente
servicio-cursos	Felipe	⏳ Pendiente
📐 Flujo de Trabajo (Git)

    Siempre hacer git pull origin main antes de empezar a trabajar.

    No subir la carpeta datos_mysql (ya está en el .gitignore).

    Al terminar una funcionalidad, hacer push y avisar al equipo.

## 🚀 Guía de Inicio Rápido (Para el Equipo)

### 1. Requisitos Previos
* Tener instalado **Docker Desktop** (Windows) o **Docker Engine** (Linux).
* **IntelliJ IDEA** instalado.

### 2. Levantar la Infraestructura
Antes de ejecutar cualquier microservicio, abre una terminal en la carpeta raíz y ejecuta:
```bash
docker compose up -d

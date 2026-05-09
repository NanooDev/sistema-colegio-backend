# Servicio de Calificaciones

Un servicio REST para gestionar calificaciones de estudiantes en cursos, desarrollado con Spring Boot siguiendo el patrón CSR (Controller-Service-Repository).

Cada calificación incluye 3 notas obligatorias (nota1, nota2, nota3) en formato decimal (máximo 7.0), calcula automáticamente la nota final como el promedio, y determina el estado como "Aprobado" si notaFinal >= 4.0, o "Reprobado" si < 4.0.

## Tecnologías
- Spring Boot 4.0.6
- Java 21
- MySQL
- JPA/Hibernate
- Flyway
- Lombok
- Validation

## Modelo de Datos
- **estudianteId**: ID del estudiante
- **cursoId**: ID del curso
- **nota1, nota2, nota3**: Notas individuales (1.0 - 7.0, decimales)
- **notaFinal**: Promedio calculado automáticamente
- **estado**: "Aprobado" o "Reprobado" basado en notaFinal
- **fecha**: Fecha de la calificación

## Validaciones
- Notas: Mínimo 1.0, máximo 7.0
- Estado: Calculado automáticamente

## Endpoints
- POST /api/v1/calificaciones - Crear calificación
- GET /api/v1/calificaciones - Listar todas
- GET /api/v1/calificaciones/{id} - Buscar por ID
- GET /api/v1/calificaciones/estudiante/{estudianteId} - Calificaciones de un estudiante
- GET /api/v1/calificaciones/curso/{cursoId} - Calificaciones de un curso
- PUT /api/v1/calificaciones/{id} - Actualizar calificación
- DELETE /api/v1/calificaciones/{id} - Eliminar calificación

## Configuración
Crear base de datos `calificaciones` en MySQL y configurar application.properties.
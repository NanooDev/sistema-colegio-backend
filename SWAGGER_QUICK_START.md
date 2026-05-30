# 🚀 Guía Rápida: Swagger en Sistema de Colegio

## Inicio Rápido

### 1️⃣ Compila e Inicia un Servicio

```bash
# Navega a cualquier servicio
cd servicio-estudiantes

# Compila y ejecuta
./mvnw clean install
./mvnw spring-boot:run
```

### 2️⃣ Accede a Swagger

Abre tu navegador y ve a:
```
http://localhost:8081/doc/swagger-ui.html
```

> Reemplaza `8081` con el puerto del servicio que iniciaste.

---

## 📋 Puertos por Servicio

```
Asignaturas      → 8085
Asistencias      → 8083
Biblioteca       → 8087
Calificaciones   → 8095
Cursos           → 8084
Estudiantes      → 8081 ⭐ (ejemplo arriba)
Finanzas         → 8088
Matrículas       → 8086
Notificaciones   → 8089
Profesores       → 8082
```

---

## ✨ Lo que Puedes Hacer en Swagger

1. **Ver todos los endpoints** - Observa qué APIs están disponibles
2. **Probar endpoints** - Haz peticiones HTTP sin tools externos
3. **Ver especificaciones** - Conoce los parámetros y respuestas esperadas
4. **Descargar spec** - Exporta la documentación en formato OpenAPI

---

## 📝 Ejemplo: Probar un Endpoint

1. En Swagger, busca un endpoint GET (por ejemplo: `/api/estudiantes`)
2. Haz clic en el endpoint
3. Presiona "Try it out"
4. Completa los parámetros si es necesario
5. Presiona "Execute"
6. Observa la respuesta

---

## 🔧 Troubleshooting

### Swagger no aparece
- Verifica que el puerto sea correcto
- Asegúrate de que el servicio está ejecutándose
- Intenta: `http://localhost:[puerto]/swagger-ui.html`

### Ver la especificación JSON
- URL: `http://localhost:[puerto]/v3/api-docs`

### Limpiar caché
- Presiona `Ctrl+Shift+Delete` en tu navegador
- O usa navegación privada

---

## 📚 Documentos de Referencia

- **SWAGGER_INTEGRATION.md** - Guía completa de integración
- **SWAGGER_CHECKLIST.md** - Resumen de cambios realizados
- **pom.xml** - Dependencias agregadas
- **SwaggerConfig.java** - Configuración por servicio

---

## 🎯 Próximos Pasos

Mejora la documentación agregando anotaciones en tus controladores:

```java
@RestController
@RequestMapping("/api/estudiantes")
@Tag(name = "Estudiantes API", description = "Gestión de estudiantes")
public class EstudianteController {
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por ID")
    @ApiResponse(responseCode = "200", description = "Estudiante encontrado")
    @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    public ResponseEntity<EstudianteDTO> obtenerPorId(
            @PathVariable @Parameter(description = "ID del estudiante") Long id) {
        // implementación
    }
}
```

---

**¡Listo! Disfruta documentando tus APIs con Swagger 🎉**

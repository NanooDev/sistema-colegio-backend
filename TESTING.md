# 🧪 Guía de Pruebas en Postman (Datos de Prueba)

Para probar que los tres microservicios principales (**Estudiantes**, **Profesores** y **Cursos**) están funcionando y comunicándose entre sí, debes seguir este orden exacto al ejecutar tus peticiones en Postman.

> **Importante:** Asegúrate de que tu contenedor de base de datos MySQL esté corriendo y de haber iniciado los tres servicios en diferentes terminales.
> - **Estudiantes** corre en el puerto: `8081`
> - **Profesores** corre en el puerto: `8082`
> - **Cursos** corre en el puerto: `8084`

---

## 👨‍🏫 1. Crear un Profesor
Primero, necesitamos un profesor que pueda ser el "profesor jefe" de un curso.

* **Método:** `POST`
* **URL:** `http://localhost:8082/api/profesores`
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "especialidad": "Matemáticas"
}
```
*Si fue exitoso, te devolverá el objeto creado con el `"id": 1` (u otro si ya habías creado).*

---

## 🏫 2. Crear un Curso
Ahora crearemos un curso y le asignaremos el Profesor (con el `id` que nos dio el paso anterior, asumamos que es el `1`).

* **Método:** `POST`
* **URL:** `http://localhost:8084/api/cursos`
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**
```json
{
  "nombre": "1° Medio A",
  "profesorJefeId": 1
}
```
*Si fue exitoso, devolverá el código HTTP 201 (Created) o el objeto creado con `"id": 1`.*

---

## 🧑‍🎓 3. Crear Estudiantes
Ahora que tenemos un curso, podemos matricular (crear) estudiantes y asociarlos usando su `cursoId`.

**Estudiante 1:**
* **Método:** `POST`
* **URL:** `http://localhost:8081/api/v1/estudiantes`
* **Body (JSON):**
```json
{
  "rut": "20.123.456-7",
  "nombre": "Mariano",
  "apellido": "González",
  "cursoId": 1
}
```

**Estudiante 2:**
* **Método:** `POST`
* **URL:** `http://localhost:8081/api/v1/estudiantes`
* **Body (JSON):**
```json
{
  "rut": "21.987.654-3",
  "nombre": "Álvaro",
  "apellido": "Muñoz",
  "cursoId": 1
}
```

---

## 🚀 4. LA PRUEBA FINAL: Integración de los 3 Servicios
Esta es la prueba más importante. El servicio de Cursos está configurado (vía `FeignClient`) para ir a buscar los datos del profesor jefe al *Servicio de Profesores* y la lista de alumnos al *Servicio de Estudiantes*.

* **Método:** `GET`
* **URL:** `http://localhost:8084/api/cursos/1`
*(Donde `1` al final de la URL es el ID del curso que creamos).*

**💥 Resultado Esperado (JSON):**
Postman debería devolverte un JSON grande donde el microservicio juntó la información completa como un rompecabezas:

```json
{
  "categoria": "curso",
  "id": 1,
  "nombre": "1° Medio A",
  "profesorJefeId": 1,
  "profesorJefe": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "especialidad": "Matemáticas"
  },
  "estudiantes": [
    {
      "id": 1,
      "rut": "20.123.456-7",
      "nombre": "Mariano",
      "apellido": "González",
      "cursoId": 1
    },
    {
      "id": 2,
      "rut": "21.987.654-3",
      "nombre": "Álvaro",
      "apellido": "Muñoz",
      "cursoId": 1
    }
  ]
}
```

> **🎉 Si ves esto:** ¡Felicidades! Significa que **los 3 microservicios** (Cursos, Profesores y Estudiantes) están conectados exitosamente a la base de datos de Docker y además **están comunicándose internamente entre ellos**.

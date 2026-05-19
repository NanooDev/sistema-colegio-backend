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
  "rut": "20123456-7",
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
  "rut": "21987654-3",
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
      "rut": "20123456-7",
      "nombre": "Mariano",
      "apellido": "González",
      "cursoId": 1
    },
    {
      "id": 2,
      "rut": "21987654-3",
      "nombre": "Álvaro",
      "apellido": "Muñoz",
      "cursoId": 1
    }
  ]
}
```

> **🎉 Si ves esto:** ¡Felicidades! Significa que **los 3 microservicios** (Cursos, Profesores y Estudiantes) están conectados exitosamente a la base de datos de Docker y además **están comunicándose internamente entre ellos**.

---

## 🚫 5. Pruebas de Control de Errores (Casos Negativos)
Para verificar que el sistema es robusto, debemos provocar errores intencionalmente y comprobar que la API devuelve los mensajes de error configurados (códigos 400 Bad Request, 404 Not Found y 409 Conflict), sin "romperse" ni devolver errores genéricos internos (como un 500).

### 5.1. Intentar registrar un Estudiante con un RUT que ya existe (Conflicto)
* **Método:** `POST`
* **URL:** `http://localhost:8081/api/v1/estudiantes`
* **Body:** Usa el mismo JSON de "Mariano" (RUT `20123456-7`) que ya registraste en el Paso 3.
* **Resultado Esperado:** 
  * Status: `409 Conflict`
  * Body devuelto: 
    ```json
    {
      "error": "Ya existe un estudiante con el RUT: 20123456-7"
    }
    ```

### 5.2. Intentar registrar un Estudiante con formato de RUT inválido (Validación)
* **Método:** `POST`
* **URL:** `http://localhost:8081/api/v1/estudiantes`
* **Body:** 
```json
{
  "rut": "20.123.456-7", 
  "nombre": "Prueba",
  "apellido": "Error",
  "cursoId": 1
}
```
*(También puedes probar enviando el "rut" o "nombre" completamente en blando: `""`)*
* **Resultado Esperado:**
  * Status: `400 Bad Request`
  * Body devuelto:
    ```json
    {
      "rut": "El RUT debe tener formato valido (12345678-9)"
    }
    ```

### 5.3. Generar un Get (Buscar) de un objeto que no existe en Base de datos (Not Found)
* **Método:** `GET`
* **URL:** `http://localhost:8081/api/v1/estudiantes/999` (Asumiendo que el ID 999 no existe)
* **Resultado Esperado:**
  * Status: `404 Not Found`
  * Body devuelto:
    ```json
    {
      "error": "Estudiante no encontrado con id: 999"
    }
    ```
*(Puedes replicar esto mismo para Cursos y Profesores usando las URL respectivas y comprobando que devuelvan el JSON del error controlado).*

---
**Nota sobre validación exhaustiva entre Microservicios (Feign):**
En un entorno avanzado, si se ingresa un estudiante a un `cursoId` que a su vez no existe (como `cursoId: 99`), el `servicio-estudiantes` debería conectarse por interfaz Feign Client hacia `servicio-cursos` para comprobar la existencia del curso antes de matricular. Actualmente el sistema confía en la ID entregada y `servicio-cursos` se encarga de acoplarlo con el GET Principal. Esto es normal en arquitecturas de primer nivel de escolaridad.


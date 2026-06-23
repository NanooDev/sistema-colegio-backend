# Pruebas Unitarias y Cobertura de Reglas de Negocio

Este documento detalla el plan de pruebas, las reglas críticas del servicio y su respectiva cobertura de testing automatizado mediante JUnit 5 y Mockito.

## Reglas de Negocio Críticas del Servicio de Biblioteca

1. **Validación de Código Único de Carrera:** No se puede crear una carrera si el código ya existe en el repositorio.
2. **Capacidad de Sala:** Una sala debe tener una capacidad mayor a 0 para poder ser registrada.
3. **Validación de Fechas en Reserva:** La fecha y hora de cierre de una reserva debe ser posterior a la fecha y hora solicitada.

## Cobertura Actual de Pruebas Unitarias

| Regla | Estado | Casos Cubiertos | Checklist |
| :--- | :--- | :--- | :--- |
| 1. Validación de Código Único | **Cubierta** | Creación exitosa (feliz), Código duplicado (error). | Caso feliz, Caso de error. |
| 2. Capacidad de Sala | **Cubierta** | Sala con capacidad 50 (feliz), Sala con capacidad 0 (error). | Regla crítica tiene test. |
| 3. Fechas en Reserva | **Pendiente** | (Solo caso feliz de fechas consistentes). | Falta testear el caso de error de fecha invertida. |

## Reflexión y Deuda Técnica

* **Riesgo sin probar:** La regla *Fechas en Reserva* no tiene un test explícito de error (cuando un estudiante intenta reservar con fecha de cierre anterior a la de inicio). Si esto se viola, el servicio podría registrar reservas inválidas o superpuestas.
* **Acción Futura:** Agregar test unitario para la validación de fechas (Regla 3) manejando las excepciones correspondientes.

Pasos para levantar el entorno local rápidamente

1) Levantar MySQL (docker)

```bash
# desde la raíz del repo
docker compose -f docker-compose.local.yml up -d
```

Esto crea un contenedor `mysql-sch` y ejecuta `scripts/mysql-init/init.sql` para crear `db_estudiantes` y `db_profesores`.

2) Arrancar los microservicios (en background)

```bash
# opcion manual (una terminal por servicio)
cd servicio-estudiantes
chmod +x mvnw
./mvnw -DskipTests spring-boot:run

cd ../servicio-profesores
chmod +x mvnw
./mvnw -DskipTests spring-boot:run
```

O bien usa el helper (automático):

```bash
chmod +x scripts/start_services.sh
./scripts/start_services.sh
```

3) Verificar

Accede a las Swagger UI:

http://localhost:8081/doc/swagger-ui.html  (servicio-estudiantes)
http://localhost:8082/doc/swagger-ui.html  (servicio-profesores)

Ver logs:

```bash
tail -f servicio-estudiantes/logs/out-estudiantes.log servicio-profesores/logs/out-profesores.log
```

Notas:
- Las contraseñas y puertos están definidas en `servicio-*/src/main/resources/application.properties`.
- Este setup es para desarrollo local sólo. No usar estas credenciales en producción.

### Instrucciones para Windows (PowerShell)

1) Requisitos previos:

- Tener Docker Desktop instalado y arrancado.
- Tener Java 21 instalado y `java`/`mvn` disponibles en PATH si no usas el wrapper.

2) Levantar MySQL (docker compose):

Abre PowerShell en la carpeta raíz del repositorio y ejecuta:

```powershell
docker compose -f docker-compose.local.yml up -d
```

3) Arrancar los microservicios (PowerShell)

Opción manual (una ventana de PowerShell por servicio):

```powershell
cd .\servicio-estudiantes
.\mvnw -DskipTests spring-boot:run

cd ..\servicio-profesores
.\mvnw -DskipTests spring-boot:run
```

Si `mvnw` no tiene permisos de ejecución en Windows, ejecutar `mvn` si lo tienes instalado:

```powershell
mvn -DskipTests spring-boot:run
```

Opción automática (PowerShell):

```powershell
.\scripts\start_services.sh
```

Nota: el script `start_services.sh` es un script bash; en Windows con Git Bash o WSL funcionará bien. Si usas PowerShell/nativo Windows sin WSL, arranca los servicios manualmente con los pasos anteriores.

4) Verificar

Abrir en navegador:

http://localhost:8081/doc/swagger-ui.html  (servicio-estudiantes)
http://localhost:8082/doc/swagger-ui.html  (servicio-profesores)

Seguir logs (PowerShell):

```powershell
Get-Content -Path .\servicio-estudiantes\logs\out-estudiantes.log -Wait -Tail 100
Get-Content -Path .\servicio-profesores\logs\out-profesores.log -Wait -Tail 100
```


#!/usr/bin/env python3
"""Genera microservicios CRUD alineados con servicio-estudiantes (Liquibase, JPA, DTO, validación)."""
import os

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def cap(name: str) -> str:
    return name[0].upper() + name[1:] if name else name


POM = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
\txsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
\t<modelVersion>4.0.0</modelVersion>
\t<parent>
\t\t<groupId>org.springframework.boot</groupId>
\t\t<artifactId>spring-boot-starter-parent</artifactId>
\t\t<version>4.0.6</version>
\t\t<relativePath/>
\t</parent>
\t<groupId>com.duoc</groupId>
\t<artifactId>{artifact}</artifactId>
\t<version>0.0.1-SNAPSHOT</version>
\t<properties>
\t\t<java.version>21</java.version>
\t</properties>
\t<dependencies>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-data-jpa</artifactId>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-liquibase</artifactId>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-validation</artifactId>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-webmvc</artifactId>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>com.mysql</groupId>
\t\t\t<artifactId>mysql-connector-j</artifactId>
\t\t\t<scope>runtime</scope>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.projectlombok</groupId>
\t\t\t<artifactId>lombok</artifactId>
\t\t\t<optional>true</optional>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-data-jpa-test</artifactId>
\t\t\t<scope>test</scope>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-liquibase-test</artifactId>
\t\t\t<scope>test</scope>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-validation-test</artifactId>
\t\t\t<scope>test</scope>
\t\t</dependency>
\t\t<dependency>
\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t<artifactId>spring-boot-starter-webmvc-test</artifactId>
\t\t\t<scope>test</scope>
\t\t</dependency>
\t</dependencies>
\t<build>
\t\t<plugins>
\t\t\t<plugin>
\t\t\t\t<groupId>org.springframework.boot</groupId>
\t\t\t\t<artifactId>spring-boot-maven-plugin</artifactId>
\t\t\t\t<configuration>
\t\t\t\t\t<excludes>
\t\t\t\t\t\t<exclude>
\t\t\t\t\t\t\t<groupId>org.projectlombok</groupId>
\t\t\t\t\t\t\t<artifactId>lombok</artifactId>
\t\t\t\t\t\t</exclude>
\t\t\t\t\t</excludes>
\t\t\t\t</configuration>
\t\t\t</plugin>
\t\t\t<plugin>
\t\t\t\t<groupId>org.apache.maven.plugins</groupId>
\t\t\t\t<artifactId>maven-compiler-plugin</artifactId>
\t\t\t\t<executions>
\t\t\t\t\t<execution>
\t\t\t\t\t\t<id>default-compile</id>
\t\t\t\t\t\t<phase>compile</phase>
\t\t\t\t\t\t<goals>
\t\t\t\t\t\t\t<goal>compile</goal>
\t\t\t\t\t\t</goals>
\t\t\t\t\t\t<configuration>
\t\t\t\t\t\t\t<annotationProcessorPaths>
\t\t\t\t\t\t\t\t<path>
\t\t\t\t\t\t\t\t\t<groupId>org.projectlombok</groupId>
\t\t\t\t\t\t\t\t\t<artifactId>lombok</artifactId>
\t\t\t\t\t\t\t\t</path>
\t\t\t\t\t\t\t</annotationProcessorPaths>
\t\t\t\t\t\t</configuration>
\t\t\t\t\t</execution>
\t\t\t\t\t<execution>
\t\t\t\t\t\t<id>default-testCompile</id>
\t\t\t\t\t\t<phase>test-compile</phase>
\t\t\t\t\t\t<goals>
\t\t\t\t\t\t\t<goal>testCompile</goal>
\t\t\t\t\t\t</goals>
\t\t\t\t\t\t<configuration>
\t\t\t\t\t\t\t<annotationProcessorPaths>
\t\t\t\t\t\t\t\t<path>
\t\t\t\t\t\t\t\t\t<groupId>org.projectlombok</groupId>
\t\t\t\t\t\t\t\t\t<artifactId>lombok</artifactId>
\t\t\t\t\t\t\t\t</path>
\t\t\t\t\t\t\t</annotationProcessorPaths>
\t\t\t\t\t\t</configuration>
\t\t\t\t\t</execution>
\t\t\t\t</executions>
\t\t\t</plugin>
\t\t</plugins>
\t</build>
</project>
"""

MAESTRO = """<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
  http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.3.xsd">
    <include file="db/changelog/v1_tabla.sql" relativeToChangelogFile="false"/>
</databaseChangeLog>
"""

PROPS = """spring.application.name={artifact}
server.port={port}
spring.datasource.url=jdbc:mysql://localhost:3306/{db}?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.liquibase.change-log=classpath:db/changelog/maestro.xml
spring.liquibase.enabled=true
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
"""


def write(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)


def gen(s):
    artifact = s["artifact"]
    pkg = s["pkg"]
    main = s["main"]
    entity = s["entity"]
    table = s["table"]
    api = s["api"]
    port = s["port"]
    db = s["db"]
    cat = s["categoria"]
    sql = s["sql"]
    fields = s["fields"]

    root = os.path.join(BASE, artifact)
    java = os.path.join(root, "src/main/java/com/duoc", pkg)
    tst = os.path.join(root, "src/test/java/com/duoc", pkg)
    res = os.path.join(root, "src/main/resources/db/changelog")

    write(os.path.join(root, "pom.xml"), POM.format(artifact=artifact))
    write(os.path.join(res, "maestro.xml"), MAESTRO)
    write(os.path.join(res, "v1_tabla.sql"), "-- changeset duoc:1\n" + sql)
    write(os.path.join(root, "src/main/resources/application.properties"), PROPS.format(artifact=artifact, port=port, db=db))

    cols_lines = []
    for f in fields:
        line = f'    @Column(name = "{f["col"]}"'
        if f.get("nullable") is False:
            line += ", nullable = false"
        line += f")\n    private {f['javaType']} {f['javaName']};"
        cols_lines.append(line)

    imports_entity = "import jakarta.persistence.*;\nimport lombok.Data;\n"
    for t in ("LocalDate", "LocalDateTime", "BigDecimal"):
        if any(x["javaType"] == t for x in fields):
            if t == "BigDecimal":
                imports_entity += "import java.math.BigDecimal;\n"
            else:
                imports_entity += f"import java.time.{t};\n"

    entity_java = f"""package com.duoc.{pkg}.model;

{imports_entity}
@Entity
@Table(name = "{table}")
@Data
public class {entity} {{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

{chr(10).join(cols_lines)}
}}
"""
    write(os.path.join(java, "model", f"{entity}.java"), entity_java)

    repo = f"""package com.duoc.{pkg}.repository;

import com.duoc.{pkg}.model.{entity};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface {entity}Repository extends JpaRepository<{entity}, Long> {{
}}
"""
    write(os.path.join(java, "repository", f"{entity}Repository.java"), repo)

    dto_order = ["categoria", "id"] + [f["javaName"] for f in fields]
    order_str = ", ".join(f'"{x}"' for x in dto_order)
    json_order = "@JsonPropertyOrder({ " + order_str + " })"

    imports_dto = "import com.fasterxml.jackson.annotation.JsonPropertyOrder;\nimport lombok.Data;\n"
    if any(x["javaType"] == "LocalDate" for x in fields):
        imports_dto += "import java.time.LocalDate;\n"
    if any(x["javaType"] == "LocalDateTime" for x in fields):
        imports_dto += "import java.time.LocalDateTime;\n"
    if any(x["javaType"] == "BigDecimal" for x in fields):
        imports_dto += "import java.math.BigDecimal;\n"

    dto_fields = f'    private String categoria = "{cat}";\n\n    private Long id;\n' + "\n".join(
        f"    private {f['javaType']} {f['javaName']};" for f in fields
    )

    dto_java = f"""package com.duoc.{pkg}.dto;

{imports_dto}
@Data
{json_order}
public class {entity}DTO {{
{dto_fields}
}}
"""
    write(os.path.join(java, "dto", f"{entity}DTO.java"), dto_java)

    imports_req = "import jakarta.validation.constraints.*;\nimport lombok.Data;\n"
    if any(x["javaType"] == "LocalDate" for x in fields):
        imports_req += "import java.time.LocalDate;\n"
    if any(x["javaType"] == "LocalDateTime" for x in fields):
        imports_req += "import java.time.LocalDateTime;\n"
    if any(x["javaType"] == "BigDecimal" for x in fields):
        imports_req += "import java.math.BigDecimal;\n"

    req_lines = []
    for f in fields:
        if f.get("req_ann"):
            req_lines.append("    " + f["req_ann"])
        req_lines.append("    private " + f["javaType"] + " " + f["javaName"] + ";")
    req_java = f"""package com.duoc.{pkg}.dto;

{imports_req}
@Data
public class {entity}Request {{
{chr(10).join(req_lines)}
}}
"""
    write(os.path.join(java, "dto", f"{entity}Request.java"), req_java)

    exc = f"""package com.duoc.{pkg}.exception;

public class {entity}NotFoundException extends RuntimeException {{
    public {entity}NotFoundException(Long id) {{
        super("{entity} no encontrado con id: " + id);
    }}
}}
"""
    write(os.path.join(java, "exception", f"{entity}NotFoundException.java"), exc)

    geh = f"""package com.duoc.{pkg}.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {{
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }}

    @ExceptionHandler({entity}NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound({entity}NotFoundException ex) {{
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }}
}}
"""
    write(os.path.join(java, "exception", "GlobalExceptionHandler.java"), geh)

    setters_new = "\n        ".join(
        f"e.set{cap(f['javaName'])}(request.get{cap(f['javaName'])}());" for f in fields
    )
    setters_upd = "\n        ".join(
        f"ex.set{cap(f['javaName'])}(request.get{cap(f['javaName'])}());" for f in fields
    )
    dto_setters = "\n        ".join(
        f"dto.set{cap(f['javaName'])}(e.get{cap(f['javaName'])}());" for f in fields
    )

    svc = f"""package com.duoc.{pkg}.service;

import com.duoc.{pkg}.dto.{entity}DTO;
import com.duoc.{pkg}.dto.{entity}Request;
import com.duoc.{pkg}.exception.{entity}NotFoundException;
import com.duoc.{pkg}.model.{entity};
import com.duoc.{pkg}.repository.{entity}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class {entity}Service {{

    @Autowired
    private {entity}Repository repository;

    public {entity}DTO guardar({entity}Request request) {{
        {entity} e = new {entity}();
        {setters_new}
        return convertirADTO(repository.save(e));
    }}

    public List<{entity}DTO> listar() {{
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }}

    public {entity}DTO buscarPorId(Long id) {{
        {entity} e = repository.findById(id).orElseThrow(() -> new {entity}NotFoundException(id));
        return convertirADTO(e);
    }}

    public {entity}DTO actualizar(Long id, {entity}Request request) {{
        {entity} ex = repository.findById(id).orElseThrow(() -> new {entity}NotFoundException(id));
        {setters_upd}
        return convertirADTO(repository.save(ex));
    }}

    public void eliminar(Long id) {{
        repository.findById(id).orElseThrow(() -> new {entity}NotFoundException(id));
        repository.deleteById(id);
    }}

    private {entity}DTO convertirADTO({entity} e) {{
        {entity}DTO dto = new {entity}DTO();
        dto.setCategoria("{cat}");
        dto.setId(e.getId());
        {dto_setters}
        return dto;
    }}
}}
"""
    write(os.path.join(java, "service", f"{entity}Service.java"), svc)

    ctl = f"""package com.duoc.{pkg}.controller;

import com.duoc.{pkg}.dto.{entity}DTO;
import com.duoc.{pkg}.dto.{entity}Request;
import com.duoc.{pkg}.service.{entity}Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{api}")
public class {entity}Controller {{

    @Autowired
    private {entity}Service service;

    @PostMapping
    public ResponseEntity<{entity}DTO> guardar(@Valid @RequestBody {entity}Request request) {{
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }}

    @GetMapping
    public ResponseEntity<List<{entity}DTO>> listar() {{
        List<{entity}DTO> lista = service.listar();
        if (lista.isEmpty()) {{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }}
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{entity}DTO> buscarPorId(@PathVariable Long id) {{
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }}

    @PutMapping("/{{id}}")
    public ResponseEntity<{entity}DTO> actualizar(@PathVariable Long id, @Valid @RequestBody {entity}Request request) {{
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }}

    @DeleteMapping("/{{id}}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {{
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }}
}}
"""
    write(os.path.join(java, "controller", f"{entity}Controller.java"), ctl)

    app = f"""package com.duoc.{pkg};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class {main} {{

    public static void main(String[] args) {{
        SpringApplication.run({main}.class, args);
    }}
}}
"""
    write(os.path.join(java, f"{main}.java"), app)

    test = f"""package com.duoc.{pkg};

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class {main}Tests {{

    @Test
    void contextLoads() {{
    }}
}}
"""
    write(os.path.join(tst, f"{main}Tests.java"), test)


SERVICES = [
    {
        "artifact": "servicio-asistencias",
        "pkg": "servicio_asistencias",
        "main": "ServicioAsistenciasApplication",
        "entity": "Asistencia",
        "table": "asistencias",
        "api": "asistencias",
        "port": "8083",
        "db": "db_asistencias",
        "categoria": "asistencia",
        "sql": """CREATE TABLE asistencias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    presente BOOLEAN NOT NULL
);""",
        "fields": [
            {"javaName": "estudianteId", "javaType": "Long", "col": "estudiante_id", "nullable": False,
             "req_ann": '@NotNull(message = "El id del estudiante es obligatorio")'},
            {"javaName": "cursoId", "javaType": "Long", "col": "curso_id", "nullable": False,
             "req_ann": '@NotNull(message = "El id del curso es obligatorio")'},
            {"javaName": "fecha", "javaType": "LocalDate", "col": "fecha", "nullable": False,
             "req_ann": '@NotNull(message = "La fecha es obligatoria")'},
            {"javaName": "presente", "javaType": "Boolean", "col": "presente", "nullable": False,
             "req_ann": '@NotNull(message = "El campo presente es obligatorio")'},
        ],
    },
    {
        "artifact": "servicio-asignaturas",
        "pkg": "servicio_asignaturas",
        "main": "ServicioAsignaturasApplication",
        "entity": "Asignatura",
        "table": "asignaturas",
        "api": "asignaturas",
        "port": "8085",
        "db": "db_asignaturas",
        "categoria": "asignatura",
        "sql": """CREATE TABLE asignaturas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE
);""",
        "fields": [
            {"javaName": "nombre", "javaType": "String", "col": "nombre", "nullable": False,
             "req_ann": '@NotBlank(message = "El nombre es obligatorio")'},
            {"javaName": "codigo", "javaType": "String", "col": "codigo", "nullable": False,
             "req_ann": '@NotBlank(message = "El codigo es obligatorio")'},
        ],
    },
    {
        "artifact": "servicio-matriculas",
        "pkg": "servicio_matriculas",
        "main": "ServicioMatriculasApplication",
        "entity": "Matricula",
        "table": "matriculas",
        "api": "matriculas",
        "port": "8086",
        "db": "db_matriculas",
        "categoria": "matricula",
        "sql": """CREATE TABLE matriculas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    anio_escolar VARCHAR(20) NOT NULL
);""",
        "fields": [
            {"javaName": "estudianteId", "javaType": "Long", "col": "estudiante_id", "nullable": False,
             "req_ann": '@NotNull(message = "El id del estudiante es obligatorio")'},
            {"javaName": "cursoId", "javaType": "Long", "col": "curso_id", "nullable": False,
             "req_ann": '@NotNull(message = "El id del curso es obligatorio")'},
            {"javaName": "anioEscolar", "javaType": "String", "col": "anio_escolar", "nullable": False,
             "req_ann": '@NotBlank(message = "El año escolar es obligatorio")'},
        ],
    },
    {
        "artifact": "servicio-biblioteca",
        "pkg": "servicio_biblioteca",
        "main": "ServicioBibliotecaApplication",
        "entity": "Libro",
        "table": "libros",
        "api": "libros",
        "port": "8087",
        "db": "db_biblioteca",
        "categoria": "libro",
        "sql": """CREATE TABLE libros (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(200) NOT NULL,
    ejemplares_disponibles INT NOT NULL
);""",
        "fields": [
            {"javaName": "titulo", "javaType": "String", "col": "titulo", "nullable": False,
             "req_ann": '@NotBlank(message = "El titulo es obligatorio")'},
            {"javaName": "autor", "javaType": "String", "col": "autor", "nullable": False,
             "req_ann": '@NotBlank(message = "El autor es obligatorio")'},
            {"javaName": "ejemplaresDisponibles", "javaType": "Integer", "col": "ejemplares_disponibles", "nullable": False,
             "req_ann": '@NotNull(message = "Los ejemplares disponibles son obligatorios")'},
        ],
    },
    {
        "artifact": "servicio-finanzas",
        "pkg": "servicio_finanzas",
        "main": "ServicioFinanzasApplication",
        "entity": "Cargo",
        "table": "cargos",
        "api": "cargos",
        "port": "8088",
        "db": "db_finanzas",
        "categoria": "cargo",
        "sql": """CREATE TABLE cargos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NULL,
    concepto VARCHAR(200) NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_pago DATE NOT NULL
);""",
        "fields": [
            {"javaName": "estudianteId", "javaType": "Long", "col": "estudiante_id", "nullable": True,
             "req_ann": None},
            {"javaName": "concepto", "javaType": "String", "col": "concepto", "nullable": False,
             "req_ann": '@NotBlank(message = "El concepto es obligatorio")'},
            {"javaName": "monto", "javaType": "BigDecimal", "col": "monto", "nullable": False,
             "req_ann": '@NotNull(message = "El monto es obligatorio")'},
            {"javaName": "fechaPago", "javaType": "LocalDate", "col": "fecha_pago", "nullable": False,
             "req_ann": '@NotNull(message = "La fecha de pago es obligatoria")'},
        ],
    },
    {
        "artifact": "servicio-notificaciones",
        "pkg": "servicio_notificaciones",
        "main": "ServicioNotificacionesApplication",
        "entity": "Notificacion",
        "table": "notificaciones",
        "api": "notificaciones",
        "port": "8089",
        "db": "db_notificaciones",
        "categoria": "notificacion",
        "sql": """CREATE TABLE notificaciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    destinatario VARCHAR(200) NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL
);""",
        "fields": [
            {"javaName": "destinatario", "javaType": "String", "col": "destinatario", "nullable": False,
             "req_ann": '@NotBlank(message = "El destinatario es obligatorio")'},
            {"javaName": "asunto", "javaType": "String", "col": "asunto", "nullable": False,
             "req_ann": '@NotBlank(message = "El asunto es obligatorio")'},
            {"javaName": "mensaje", "javaType": "String", "col": "mensaje", "nullable": False,
             "req_ann": '@NotBlank(message = "El mensaje es obligatorio")'},
            {"javaName": "fechaEnvio", "javaType": "LocalDateTime", "col": "fecha_envio", "nullable": False,
             "req_ann": '@NotNull(message = "La fecha de envio es obligatoria")'},
        ],
    },
]


if __name__ == "__main__":
    for s in SERVICES:
        gen(s)
    print("generated", len(SERVICES), "services")

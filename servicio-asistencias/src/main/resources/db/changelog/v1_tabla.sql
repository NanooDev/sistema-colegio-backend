-- changeset duoc:1
CREATE TABLE asistencias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    presente BOOLEAN NOT NULL
);
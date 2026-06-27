-- liquibase formatted sql

-- changeset duoc:1
CREATE TABLE matriculas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    anio_escolar VARCHAR(20) NOT NULL
);

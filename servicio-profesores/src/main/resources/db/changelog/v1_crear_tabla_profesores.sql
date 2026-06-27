-- liquibase formatted sql

-- changeset duoc:1
CREATE TABLE profesores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    especialidad VARCHAR(150) NOT NULL
);

-- liquibase formatted sql

-- changeset duoc:1
CREATE TABLE notificaciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    destinatario VARCHAR(200) NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL
);

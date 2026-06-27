-- liquibase formatted sql

-- changeset duoc:1
CREATE TABLE cargos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NULL,
    concepto VARCHAR(200) NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_pago DATE NOT NULL
);

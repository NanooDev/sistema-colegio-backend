-- changeset duoc:1
CREATE TABLE cursos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    profesor_jefe_id BIGINT NOT NULL
);

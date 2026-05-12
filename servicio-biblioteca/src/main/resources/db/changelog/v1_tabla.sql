-- changeset duoc:1
CREATE TABLE libros (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(200) NOT NULL,
    ejemplares_disponibles INT NOT NULL
);
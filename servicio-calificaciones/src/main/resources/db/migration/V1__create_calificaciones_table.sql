CREATE TABLE `calificaciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `estudiante_id` int NOT NULL,
  `curso_id` int NOT NULL,
  `nota1` double NOT NULL,
  `nota2` double NOT NULL,
  `nota3` double NOT NULL,
  `nota_final` double NOT NULL,
  `estado` varchar(255) NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
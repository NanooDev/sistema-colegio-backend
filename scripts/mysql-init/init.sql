-- init sql: create databases used by the services
CREATE DATABASE IF NOT EXISTS db_estudiantes;
CREATE DATABASE IF NOT EXISTS db_profesores;
-- ensure root can connect from any host (development only)
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS techlab_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- Ejecutar como administrador. Modificar la contraseña antes de usar fuera de un entorno local.
CREATE USER IF NOT EXISTS 'techlab'@'localhost' IDENTIFIED BY 'CAMBIAR_ESTA_CLAVE';
GRANT ALL PRIVILEGES ON techlab_db.* TO 'techlab'@'localhost';
FLUSH PRIVILEGES;

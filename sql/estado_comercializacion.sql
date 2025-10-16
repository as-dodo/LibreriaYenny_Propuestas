-- Tabla para estados de comercialización de títulos
CREATE TABLE IF NOT EXISTS estado_comercializacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Insertar estados predefinidos según diagrama
INSERT INTO estado_comercializacion (nombre) VALUES
('EN_PREPARACION'),
('EN_PROMOCION'),
('DISPONIBLE'),
('AGOTADO');

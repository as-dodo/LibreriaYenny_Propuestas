-- Tabla para títulos creados desde propuestas aprobadas
-- Se crea cuando el editor convierte una propuesta aprobada en título
CREATE TABLE IF NOT EXISTS titulos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    propuesta_id INT NOT NULL UNIQUE,
    titulo VARCHAR(255) NOT NULL,
    estado_comercializacion_id INT NOT NULL DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (propuesta_id) REFERENCES propuestas(id) ON DELETE CASCADE,
    FOREIGN KEY (estado_comercializacion_id) REFERENCES estado_comercializacion(id)
);

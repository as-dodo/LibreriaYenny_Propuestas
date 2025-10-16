-- Tabla para condiciones de publicación
-- Se crea cuando el editor define condiciones para una propuesta aprobada
CREATE TABLE IF NOT EXISTS condiciones_publicacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    propuesta_id INT NOT NULL UNIQUE,
    tirada_inicial INT NOT NULL,
    porcentaje_ganancias_autor DECIMAL(5,2) NOT NULL,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (propuesta_id) REFERENCES propuestas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS historial_eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    propuesta_id INT NULL,
    usuario_id INT NULL,
    accion VARCHAR(50) NOT NULL,
    descripcion TEXT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (propuesta_id) REFERENCES propuestas(id) ON DELETE SET NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

CREATE INDEX idx_historial_propuesta ON historial_eventos(propuesta_id);
CREATE INDEX idx_historial_usuario ON historial_eventos(usuario_id);
CREATE INDEX idx_historial_fecha ON historial_eventos(fecha_hora);

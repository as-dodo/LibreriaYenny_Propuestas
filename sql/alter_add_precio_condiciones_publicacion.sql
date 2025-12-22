-- Agrega el precio por ejemplar a la tabla de condiciones ya existente
ALTER TABLE condiciones_publicacion
    ADD COLUMN precio DECIMAL(10,2) NOT NULL DEFAULT 0 AFTER porcentaje_ganancias_autor;

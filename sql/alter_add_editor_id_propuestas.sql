-- Agrega el editor_id a una tabla de propuestas ya existente
ALTER TABLE propuestas
    ADD COLUMN editor_id INT NULL AFTER escritor_id;

ALTER TABLE propuestas
    ADD CONSTRAINT fk_propuestas_editor
        FOREIGN KEY (editor_id) REFERENCES usuarios(id) ON DELETE SET NULL;

CREATE INDEX idx_propuestas_editor ON propuestas(editor_id);

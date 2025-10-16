CREATE TABLE propuestas_comentarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  propuesta_id INT NOT NULL,
  comentarios TEXT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (propuesta_id) REFERENCES propuestas(id)
);

CREATE TABLE propuestas (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  escritor_id      INT          NOT NULL,
  titulo_propuesto VARCHAR(200) NOT NULL,
  resumen          TEXT         NOT NULL,
  archivo_url      VARCHAR(500) NULL,
  estado ENUM('BORRADOR','ENVIADA','EN_REVISION','APROBADA','RECHAZADA')
         NOT NULL DEFAULT 'ENVIADA',
  fecha_creacion   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_decision   DATETIME     NULL,
  CONSTRAINT fk_propuestas_escritor
      FOREIGN KEY (escritor_id) REFERENCES usuarios(id)
);
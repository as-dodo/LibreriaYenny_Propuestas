package ar.edu.acn3bv.libreria.propuestas.domain;

import java.time.LocalDateTime;

public class Propuesta {
    private int id;
    private String titulo;
    private String sinopsis;
    private int escritorId;
    private EstadoPropuesta estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Propuesta() {}

    public Propuesta(String titulo, String sinopsis, int escritorId) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.escritorId = escritorId;
        this.estado = EstadoPropuesta.BORRADOR;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = this.fechaCreacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; touch(); }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; touch(); }

    public int getEscritorId() { return escritorId; }
    public void setEscritorId(int escritorId) { this.escritorId = escritorId; touch(); }

    public EstadoPropuesta getEstado() { return estado; }
    public void setEstado(EstadoPropuesta estado) { this.estado = estado; touch(); }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    private void touch() { this.fechaActualizacion = LocalDateTime.now(); }
}

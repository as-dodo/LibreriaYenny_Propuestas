package bll.propuestas;

import java.time.LocalDateTime;

public class Propuesta {
    private int id;
    private String tituloPropuesto;
    private String resumen;
    private String archivoUrl;
    private EstadoPropuesta estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDecision;

    public Propuesta(int id,
                     String tituloPropuesto,
                     String resumen,
                     String archivoUrl,
                     EstadoPropuesta estado,
                     LocalDateTime fechaCreacion,
                     LocalDateTime fechaDecision) {
        this.id = id;
        this.tituloPropuesto = tituloPropuesto;
        this.resumen = resumen;
        this.archivoUrl = archivoUrl;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaDecision = fechaDecision;
    }

    // getters / setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTituloPropuesto() { return tituloPropuesto; }
    public void setTituloPropuesto(String tituloPropuesto) { this.tituloPropuesto = tituloPropuesto; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public String getArchivoUrl() { return archivoUrl; }
    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }

    public EstadoPropuesta getEstado() { return estado; }
    public void setEstado(EstadoPropuesta estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaDecision() { return fechaDecision; }
    public void setFechaDecision(LocalDateTime fechaDecision) { this.fechaDecision = fechaDecision; }
}

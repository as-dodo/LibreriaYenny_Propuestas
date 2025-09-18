package models.eventos;

import java.time.LocalDateTime;

public class HistorialEvento {

    private String id;
    private LocalDateTime fechaHora;
    private String usuarioId;
    private AccionEvento accion;
    private String referencia;

    public HistorialEvento(String id, LocalDateTime fechaHora, String usuarioId, AccionEvento accion, String referencia) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.referencia = referencia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public AccionEvento getAccion() {
        return accion;
    }

    public void setAccion(AccionEvento accion) {
        this.accion = accion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    @Override
    public String toString() {
        return "HistorialEvento{" +
                "id='" + id + '\'' +
                ", fechaHora=" + fechaHora +
                ", usuarioId='" + usuarioId + '\'' +
                ", accion=" + accion +
                ", referencia='" + referencia + '\'' +
                '}';
    }
}

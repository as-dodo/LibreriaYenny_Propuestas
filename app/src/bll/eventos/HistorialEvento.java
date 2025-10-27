package bll.eventos;

import java.time.LocalDateTime;

public class HistorialEvento {

    private Integer id;
    private Integer propuestaId;
    private Integer usuarioId;
    private AccionEvento accion;
    private String descripcion;
    private LocalDateTime fechaHora;

    public HistorialEvento(Integer id, Integer propuestaId, Integer usuarioId, AccionEvento accion, String descripcion, LocalDateTime fechaHora) {
        this.id = id;
        this.propuestaId = propuestaId;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public HistorialEvento(Integer propuestaId, Integer usuarioId, AccionEvento accion, String descripcion) {
        this.propuestaId = propuestaId;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPropuestaId() {
        return propuestaId;
    }

    public void setPropuestaId(Integer propuestaId) {
        this.propuestaId = propuestaId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public AccionEvento getAccion() {
        return accion;
    }

    public void setAccion(AccionEvento accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        return "HistorialEvento{" +
                "id=" + id +
                ", propuestaId=" + propuestaId +
                ", usuarioId=" + usuarioId +
                ", accion=" + accion +
                ", descripcion='" + descripcion + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}

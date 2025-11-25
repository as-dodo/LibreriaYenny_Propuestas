package bll.views;
import bll.propuestas.EstadoPropuesta;
import java.time.LocalDateTime;

public class PropuestaEditorView {
    private int id;
    private String autor;
    private String titulo;
    private String resumen;
    private EstadoPropuesta estado;
    private LocalDateTime fechaCreacion;

    public PropuestaEditorView(int id,
                               String autor,
                               String titulo,
                               String resumen,
                               EstadoPropuesta estado,
                               LocalDateTime fechaCreacion) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.resumen = resumen;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() { return id; }
    public String getAutor() { return autor; }
    public String getTitulo() { return titulo; }
    public String getResumen() { return resumen; }
    public EstadoPropuesta getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}

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
    private String archivoUrl;

    public PropuestaEditorView(int id,
                               String autor,
                               String titulo,
                               String resumen,
                               EstadoPropuesta estado,
                               LocalDateTime fechaCreacion,
                               String archivoUrl) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.resumen = resumen;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.archivoUrl = archivoUrl;
    }

    public int getId() { return id; }
    public String getAutor() { return autor; }
    public String getTitulo() { return titulo; }
    public String getResumen() { return resumen; }
    public EstadoPropuesta getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getArchivoUrl() { return archivoUrl; }
}

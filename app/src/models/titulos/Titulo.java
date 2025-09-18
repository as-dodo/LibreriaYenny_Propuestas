package models.titulos;

public class Titulo {
    private String id;
    private String titulo;
    private String autor;
    private String descripcionCorta;
    private EstadoComercializacion estadoComercializacion;

    public Titulo(String id,
                  String titulo,
                  String autor,
                  String descripcionCorta,
                  EstadoComercializacion estadoComercializacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.descripcionCorta = descripcionCorta;
        this.estadoComercializacion = estadoComercializacion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getDescripcionCorta() { return descripcionCorta; }
    public void setDescripcionCorta(String descripcionCorta) { this.descripcionCorta = descripcionCorta; }

    public EstadoComercializacion getEstadoComercializacion() { return estadoComercializacion; }
    public void setEstadoComercializacion(EstadoComercializacion estadoComercializacion) {
        this.estadoComercializacion = estadoComercializacion;
    }
}

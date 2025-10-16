package bll.titulos;

public class Titulo {
    private Integer id;
    private int propuestaId;
    private String titulo;
    private EstadoComercializacion estadoComercializacion;

    public Titulo(Integer id, int propuestaId, String titulo, EstadoComercializacion estadoComercializacion) {
        this.id = id;
        this.propuestaId = propuestaId;
        this.titulo = titulo;
        this.estadoComercializacion = estadoComercializacion;
    }

    public Titulo(int propuestaId, String titulo, EstadoComercializacion estadoComercializacion) {
        this.propuestaId = propuestaId;
        this.titulo = titulo;
        this.estadoComercializacion = estadoComercializacion;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getPropuestaId() { return propuestaId; }
    public void setPropuestaId(int propuestaId) { this.propuestaId = propuestaId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public EstadoComercializacion getEstadoComercializacion() { return estadoComercializacion; }
    public void setEstadoComercializacion(EstadoComercializacion estadoComercializacion) {
        this.estadoComercializacion = estadoComercializacion;
    }
}

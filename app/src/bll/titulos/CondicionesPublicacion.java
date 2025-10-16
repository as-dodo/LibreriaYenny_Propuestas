package bll.titulos;

public class CondicionesPublicacion {
    private Integer id;
    private int propuestaId;
    private int tiradaInicial;
    private double porcentajeGananciasAutor;
    private String observaciones;

    public CondicionesPublicacion(Integer id, int propuestaId, int tiradaInicial,
                                  double porcentajeGananciasAutor, String observaciones) {
        this.id = id;
        this.propuestaId = propuestaId;
        this.tiradaInicial = tiradaInicial;
        this.porcentajeGananciasAutor = porcentajeGananciasAutor;
        this.observaciones = observaciones;
    }

    public CondicionesPublicacion(int propuestaId, int tiradaInicial,
                                  double porcentajeGananciasAutor, String observaciones) {
        this.propuestaId = propuestaId;
        this.tiradaInicial = tiradaInicial;
        this.porcentajeGananciasAutor = porcentajeGananciasAutor;
        this.observaciones = observaciones;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getPropuestaId() { return propuestaId; }
    public void setPropuestaId(int propuestaId) { this.propuestaId = propuestaId; }

    public int getTiradaInicial() { return tiradaInicial; }
    public void setTiradaInicial(int tiradaInicial) { this.tiradaInicial = tiradaInicial; }

    public double getPorcentajeGananciasAutor() { return porcentajeGananciasAutor; }
    public void setPorcentajeGananciasAutor(double porcentajeGananciasAutor) { this.porcentajeGananciasAutor = porcentajeGananciasAutor; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

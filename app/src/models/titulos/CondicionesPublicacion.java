package models.titulos;

public class CondicionesPublicacion {
    private String id;
    private int tiradaInicial;
    private double porcentajeGananciasAutor;
    private String observaciones;

    public CondicionesPublicacion(String id,
                                  int tiradaInicial,
                                  double porcentajeGananciasAutor,
                                  String observaciones) {
        this.id = id;
        this.tiradaInicial = tiradaInicial;
        this.porcentajeGananciasAutor = porcentajeGananciasAutor;
        this.observaciones = observaciones;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getTiradaInicial() { return tiradaInicial; }
    public void setTiradaInicial(int tiradaInicial) { this.tiradaInicial = tiradaInicial; }

    public double getPorcentajeGananciasAutor() { return porcentajeGananciasAutor; }
    public void setPorcentajeGananciasAutor(double porcentajeGananciasAutor) { this.porcentajeGananciasAutor = porcentajeGananciasAutor; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

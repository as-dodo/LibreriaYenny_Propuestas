package bll.views;

public class LibroAprobadoView {
    private final int propuestaId;
    private final String titulo;
    private final String autor;
    private final double precio;
    private final int tiradaInicial;
    private final String estadoPropuesta;
    private final String estadoComercializacion;

    public LibroAprobadoView(int propuestaId,
                             String titulo,
                             String autor,
                             double precio,
                             int tiradaInicial,
                             String estadoPropuesta,
                             String estadoComercializacion) {
        this.propuestaId = propuestaId;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.tiradaInicial = tiradaInicial;
        this.estadoPropuesta = estadoPropuesta;
        this.estadoComercializacion = estadoComercializacion;
    }

    public int getPropuestaId() {
        return propuestaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public double getPrecio() {
        return precio;
    }

    public int getTiradaInicial() {
        return tiradaInicial;
    }

    public String getEstadoPropuesta() {
        return estadoPropuesta;
    }

    public String getEstadoComercializacion() {
        return estadoComercializacion;
    }
}

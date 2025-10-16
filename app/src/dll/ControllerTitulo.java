package dll;

import bll.titulos.CondicionesPublicacion;
import bll.titulos.EstadoComercializacion;
import bll.titulos.Titulo;
import java.sql.*;

public class ControllerTitulo {

    public boolean insertarCondiciones(Connection cn, CondicionesPublicacion condiciones) throws SQLException {
        String sql = "INSERT INTO condiciones_publicacion (propuesta_id, tirada_inicial, porcentaje_ganancias_autor, observaciones) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, condiciones.getPropuestaId());
            ps.setInt(2, condiciones.getTiradaInicial());
            ps.setDouble(3, condiciones.getPorcentajeGananciasAutor());
            ps.setString(4, condiciones.getObservaciones());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean yaExistenCondiciones(Connection cn, int propuestaId) throws SQLException {
        String sql = "SELECT 1 FROM condiciones_publicacion WHERE propuesta_id = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean insertarTitulo(Connection cn, Titulo titulo) throws SQLException {
        String sql = "INSERT INTO titulos (propuesta_id, titulo, estado_comercializacion_id) VALUES (?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, titulo.getPropuestaId());
            ps.setString(2, titulo.getTitulo());
            ps.setInt(3, getEstadoComercializacionId(titulo.getEstadoComercializacion()));
            return ps.executeUpdate() > 0;
        }
    }

    private int getEstadoComercializacionId(EstadoComercializacion estado) {
        return switch (estado) {
            case EN_PREPARACION -> 1;
            case EN_PROMOCION -> 2;
            case DISPONIBLE -> 3;
            case AGOTADO -> 4;
        };
    }

    public boolean yaExisteTitulo(Connection cn, int propuestaId) throws SQLException {
        String sql = "SELECT 1 FROM titulos WHERE propuesta_id = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String obtenerEstadoPropuesta(Connection cn, int propuestaId) throws SQLException {
        String sql = "SELECT estado FROM propuestas WHERE id = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("estado");
                return null;
            }
        }
    }
}

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

    public Titulo obtenerTituloPorId(Connection cn, int tituloId) throws SQLException {
        String sql = """
            SELECT t.id, t.propuesta_id, t.titulo, e.id as estado_id
            FROM titulos t
            JOIN estado_comercializacion e ON e.id = t.estado_comercializacion_id
            WHERE t.id = ?
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, tituloId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int estadoId = rs.getInt("estado_id");
                    EstadoComercializacion estado = getEstadoComercializacionPorId(estadoId);
                    
                    return new Titulo(
                        rs.getInt("id"),
                        rs.getInt("propuesta_id"),
                        rs.getString("titulo"),
                        estado
                    );
                }
                return null;
            }
        }
    }

    public Titulo obtenerTituloPorPropuesta(Connection cn, int propuestaId) throws SQLException {
        String sql = """
            SELECT t.id, t.propuesta_id, t.titulo, e.id as estado_id
            FROM titulos t
            JOIN estado_comercializacion e ON e.id = t.estado_comercializacion_id
            WHERE t.propuesta_id = ?
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int estadoId = rs.getInt("estado_id");
                    EstadoComercializacion estado = getEstadoComercializacionPorId(estadoId);
                    
                    return new Titulo(
                        rs.getInt("id"),
                        rs.getInt("propuesta_id"),
                        rs.getString("titulo"),
                        estado
                    );
                }
                return null;
            }
        }
    }

    private EstadoComercializacion getEstadoComercializacionPorId(int id) {
        return switch (id) {
            case 1 -> EstadoComercializacion.EN_PREPARACION;
            case 2 -> EstadoComercializacion.EN_PROMOCION;
            case 3 -> EstadoComercializacion.DISPONIBLE;
            case 4 -> EstadoComercializacion.AGOTADO;
            default -> EstadoComercializacion.EN_PREPARACION;
        };
    }

    public boolean actualizarEstadoComercializacion(Connection cn, Titulo titulo) throws SQLException {
        String sql = "UPDATE titulos SET estado_comercializacion_id = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, getEstadoComercializacionId(titulo.getEstadoComercializacion()));
            ps.setInt(2, titulo.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public java.util.List<String> listarTitulos(Connection cn) throws SQLException {
        String sql = """
            SELECT t.id, t.titulo, e.nombre as estado, t.fecha_creacion
            FROM titulos t
            JOIN estado_comercializacion e ON e.id = t.estado_comercializacion_id
            ORDER BY t.id DESC
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            java.util.List<String> filas = new java.util.ArrayList<>();
            while (rs.next()) {
                filas.add(String.format("#%d | %-30s | %-20s | %s",
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("estado"),
                        rs.getTimestamp("fecha_creacion")));
            }
            return filas;
        }
    }
}

package dll;

import bll.eventos.HistorialEvento;
import java.sql.*;

public class ControllerHistorial {

    public boolean registrarEvento(Connection cn, HistorialEvento evento) throws SQLException {
        String sql = "INSERT INTO historial_eventos (propuesta_id, usuario_id, accion, descripcion) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            if (evento.getPropuestaId() != null) {
                ps.setInt(1, evento.getPropuestaId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            
            if (evento.getUsuarioId() != null) {
                ps.setInt(2, evento.getUsuarioId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setString(3, evento.getAccion().name());
            ps.setString(4, evento.getDescripcion());
            
            return ps.executeUpdate() > 0;
        }
    }

    public java.util.List<String> listarEventosPorPropuesta(Connection cn, int propuestaId) throws SQLException {
        String sql = """
            SELECT h.id, h.accion, h.descripcion, h.fecha_hora, u.nombre as usuario
            FROM historial_eventos h
            LEFT JOIN usuarios u ON u.id = h.usuario_id
            WHERE h.propuesta_id = ?
            ORDER BY h.fecha_hora DESC
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                java.util.List<String> eventos = new java.util.ArrayList<>();
                while (rs.next()) {
                    String usuario = rs.getString("usuario");
                    eventos.add(String.format("[%s] %s - %s%s",
                            rs.getTimestamp("fecha_hora"),
                            rs.getString("accion"),
                            usuario != null ? usuario + ": " : "",
                            rs.getString("descripcion") != null ? rs.getString("descripcion") : ""));
                }
                return eventos;
            }
        }
    }
}

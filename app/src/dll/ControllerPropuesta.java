package dll;

import bll.propuestas.EstadoPropuesta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControllerPropuesta {

    public boolean insertarPropuesta(Connection cn, int escritorId, String titulo, String resumen, String archivoUrl) throws SQLException {
        String sql = """
            INSERT INTO propuestas (escritor_id, titulo_propuesto, resumen, archivo_url, estado)
            VALUES (?,?,?,?,?)
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, escritorId);
            ps.setString(2, titulo);
            ps.setString(3, resumen);
            ps.setString(4, archivoUrl);
            ps.setString(5, EstadoPropuesta.ENVIADA.name());
            return ps.executeUpdate() > 0;
        }
    }

    public List<String> listarPorEscritor(Connection cn, int escritorId) throws SQLException {
        String sql = """
            SELECT id, titulo_propuesto, estado, fecha_creacion
            FROM propuestas
            WHERE escritor_id = ?
            ORDER BY id DESC
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, escritorId);
            try (ResultSet rs = ps.executeQuery()) {
                List<String> filas = new ArrayList<>();
                while (rs.next()) {
                    filas.add(String.format("#%d | %s | %s | %s",
                            rs.getInt("id"),
                            rs.getString("titulo_propuesto"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion")));
                }
                return filas;
            }
        }
    }

    public List<String> listarBandeja(Connection cn) throws SQLException {
        String sql = """
            SELECT p.id, p.titulo_propuesto, u.nombre AS escritor, p.estado, p.fecha_creacion
            FROM propuestas p
            JOIN usuarios u ON u.id = p.escritor_id
            WHERE p.estado IN ('ENVIADA', 'EN_REVISION')
            ORDER BY p.fecha_creacion ASC
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<String> filas = new ArrayList<>();
            while (rs.next()) {
                filas.add(String.format("#%d | %-20s | %-15s | %s | %s",
                        rs.getInt("id"),
                        rs.getString("titulo_propuesto"),
                        rs.getString("escritor"),
                        rs.getString("estado"),
                        rs.getTimestamp("fecha_creacion")));
            }
            return filas;
        }
    }

    public boolean actualizarEstado(Connection cn, int propuestaId, EstadoPropuesta nuevoEstado) throws SQLException {
        String sql = """
            UPDATE propuestas
            SET estado = ?, fecha_decision = CASE
                 WHEN ? IN ('APROBADA','RECHAZADA') THEN CURRENT_TIMESTAMP
                 ELSE fecha_decision
            END
            WHERE id = ?
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, nuevoEstado.name());
            ps.setInt(3, propuestaId);
            return ps.executeUpdate() > 0;
        }
    }
}


package dll;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerReporte {

    public Map<String, Integer> obtenerEstadisticasPropuestas(Connection cn) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        String sql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN estado = 'ENVIADA' THEN 1 ELSE 0 END) as enviadas,
                SUM(CASE WHEN estado = 'EN_REVISION' THEN 1 ELSE 0 END) as en_revision,
                SUM(CASE WHEN estado = 'APROBADA' THEN 1 ELSE 0 END) as aprobadas,
                SUM(CASE WHEN estado = 'RECHAZADA' THEN 1 ELSE 0 END) as rechazadas
            FROM propuestas
            """;
        
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                estadisticas.put("total", rs.getInt("total"));
                estadisticas.put("enviadas", rs.getInt("enviadas"));
                estadisticas.put("en_revision", rs.getInt("en_revision"));
                estadisticas.put("aprobadas", rs.getInt("aprobadas"));
                estadisticas.put("rechazadas", rs.getInt("rechazadas"));
            }
        }
        
        return estadisticas;
    }

    public Map<String, Integer> obtenerEstadisticasTitulos(Connection cn) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        String sql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN e.nombre = 'EN_PREPARACION' THEN 1 ELSE 0 END) as en_preparacion,
                SUM(CASE WHEN e.nombre = 'EN_PROMOCION' THEN 1 ELSE 0 END) as en_promocion,
                SUM(CASE WHEN e.nombre = 'DISPONIBLE' THEN 1 ELSE 0 END) as disponible,
                SUM(CASE WHEN e.nombre = 'AGOTADO' THEN 1 ELSE 0 END) as agotado
            FROM titulos t
            JOIN estado_comercializacion e ON e.id = t.estado_comercializacion_id
            """;
        
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                estadisticas.put("total", rs.getInt("total"));
                estadisticas.put("en_preparacion", rs.getInt("en_preparacion"));
                estadisticas.put("en_promocion", rs.getInt("en_promocion"));
                estadisticas.put("disponible", rs.getInt("disponible"));
                estadisticas.put("agotado", rs.getInt("agotado"));
            }
        }
        
        return estadisticas;
    }

    public List<String> listarTodasPropuestas(Connection cn, String filtroEstado) throws SQLException {
        String sql;
        
        if (filtroEstado == null || filtroEstado.equals("TODAS")) {
            sql = """
                SELECT p.id, p.titulo_propuesto, u.nombre as escritor, p.estado, p.fecha_creacion
                FROM propuestas p
                JOIN usuarios u ON u.id = p.escritor_id
                ORDER BY p.fecha_creacion DESC
                """;
        } else {
            sql = """
                SELECT p.id, p.titulo_propuesto, u.nombre as escritor, p.estado, p.fecha_creacion
                FROM propuestas p
                JOIN usuarios u ON u.id = p.escritor_id
                WHERE p.estado = ?
                ORDER BY p.fecha_creacion DESC
                """;
        }
        
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            if (filtroEstado != null && !filtroEstado.equals("TODAS")) {
                ps.setString(1, filtroEstado);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                List<String> filas = new ArrayList<>();
                while (rs.next()) {
                    filas.add(String.format("#%d | %-30s | %-20s | %-15s | %s",
                            rs.getInt("id"),
                            rs.getString("titulo_propuesto"),
                            rs.getString("escritor"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion")));
                }
                return filas;
            }
        }
    }

    public List<String> obtenerTopEscritores(Connection cn, int limite) throws SQLException {
        String sql = """
            SELECT u.nombre, 
                   COUNT(p.id) as total_propuestas,
                   SUM(CASE WHEN p.estado = 'APROBADA' THEN 1 ELSE 0 END) as aprobadas
            FROM usuarios u
            JOIN propuestas p ON p.escritor_id = u.id
            WHERE u.rol = 'ESCRITOR'
            GROUP BY u.id, u.nombre
            ORDER BY total_propuestas DESC
            LIMIT ?
            """;
        
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            
            try (ResultSet rs = ps.executeQuery()) {
                List<String> escritores = new ArrayList<>();
                int posicion = 1;
                while (rs.next()) {
                    int total = rs.getInt("total_propuestas");
                    int aprobadas = rs.getInt("aprobadas");
                    double tasa = total > 0 ? (aprobadas * 100.0 / total) : 0;
                    
                    escritores.add(String.format("%d. %-25s | Total: %d | Aprobadas: %d (%.1f%%)",
                            posicion++,
                            rs.getString("nombre"),
                            total,
                            aprobadas,
                            tasa));
                }
                return escritores;
            }
        }
    }

    public Map<String, Integer> obtenerEstadisticasUsuarios(Connection cn) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        String sql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN rol = 'ESCRITOR' THEN 1 ELSE 0 END) as escritores,
                SUM(CASE WHEN rol = 'EDITOR' THEN 1 ELSE 0 END) as editores,
                SUM(CASE WHEN rol = 'ADMIN' THEN 1 ELSE 0 END) as admins
            FROM usuarios
            """;
        
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                estadisticas.put("total", rs.getInt("total"));
                estadisticas.put("escritores", rs.getInt("escritores"));
                estadisticas.put("editores", rs.getInt("editores"));
                estadisticas.put("admins", rs.getInt("admins"));
            }
        }
        
        return estadisticas;
    }
}

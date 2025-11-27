package dll;

import bll.propuestas.EstadoPropuesta;
import bll.propuestas.Propuesta;
import bll.views.PropuestaEditorView;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerPropuesta {

    public boolean insertarPropuesta(Connection cn, Propuesta propuesta) throws SQLException {
        String sql = """
                INSERT INTO propuestas (escritor_id, editor_id, titulo_propuesto, resumen, archivo_url, estado)
                VALUES (?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuesta.getEscritorId());
            if (propuesta.getEditorId() != null) {
                ps.setInt(2, propuesta.getEditorId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, propuesta.getTituloPropuesto());
            ps.setString(4, propuesta.getResumen());
            ps.setString(5, propuesta.getArchivoUrl());
            ps.setString(6, propuesta.getEstado().name());
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

    public boolean actualizarEstado(Connection cn, Propuesta propuesta) throws SQLException {
        String sql = """
                UPDATE propuestas
                SET estado = ?,
                    editor_id = COALESCE(?, editor_id),
                    fecha_decision = CASE
                     WHEN ? IN ('APROBADA','RECHAZADA') THEN CURRENT_TIMESTAMP
                     ELSE fecha_decision
                END
                WHERE id = ?
                """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, propuesta.getEstado().name());
            if (propuesta.getEditorId() != null) {
                ps.setInt(2, propuesta.getEditorId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, propuesta.getEstado().name());
            ps.setInt(4, propuesta.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public String obtenerDetalle(Connection cn, int propuestaId) throws SQLException {
        String sql = """
            SELECT id, titulo_propuesto, resumen, archivo_url, estado, fecha_creacion
            FROM propuestas
            WHERE id = ?
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String comentarios = listarComentariosTexto(cn, propuestaId);

                return """
                    ID: %d
                    Título: %s
                    Estado: %s
                    Creado: %s
                    Archivo: %s

                    Resumen:
                    %s

                    Comentarios (últimos):
                    %s
                    """.formatted(
                        rs.getInt("id"),
                        rs.getString("titulo_propuesto"),
                        rs.getString("estado"),
                        rs.getTimestamp("fecha_creacion"),
                        rs.getString("archivo_url") == null ? "-" : rs.getString("archivo_url"),
                        rs.getString("resumen"),
                        comentarios
                );
            }
        }
    }


    private String listarComentariosTexto(Connection cn, int propuestaId) throws SQLException {
        String sql = """
            SELECT u.nombre AS autor, c.comentario, c.fecha
            FROM propuestas_comentarios c
            JOIN usuarios u ON u.id = c.usuario_id
            WHERE c.propuesta_id = ?
            ORDER BY c.fecha DESC
            LIMIT 10
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    sb.append("- [")
                            .append(rs.getTimestamp("fecha"))
                            .append("] ")
                            .append(rs.getString("autor"))
                            .append(": ")
                            .append(rs.getString("comentario"))
                            .append("\n");
                }
                return any ? sb.toString() : "(sin comentarios)";
            }
        }
    }

    public boolean agregarComentario(Connection cn, int propuestaId, int usuarioId, String comentario) throws SQLException {
        String sql = """
            INSERT INTO propuestas_comentarios (propuesta_id, usuario_id, comentario, fecha)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP)
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            ps.setInt(2, usuarioId);
            ps.setString(3, comentario.trim());
            return ps.executeUpdate() > 0;
        }
    }

    public Propuesta obtenerPorId(Connection cn, int propuestaId) throws SQLException {
        String sql = """
            SELECT id, escritor_id, editor_id, titulo_propuesto, resumen, archivo_url, estado, fecha_creacion, fecha_decision
            FROM propuestas
            WHERE id = ?
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, propuestaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                EstadoPropuesta estado = EstadoPropuesta.valueOf(rs.getString("estado"));
                Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
                Timestamp tsDecision = rs.getTimestamp("fecha_decision");

                return new Propuesta(
                        rs.getInt("id"),
                        rs.getInt("escritor_id"),
                        (Integer) rs.getObject("editor_id"),
                        rs.getString("titulo_propuesto"),
                        rs.getString("resumen"),
                        rs.getString("archivo_url"),
                        estado,
                        tsCreacion != null ? tsCreacion.toLocalDateTime() : null,
                        tsDecision != null ? tsDecision.toLocalDateTime() : null
                );
            }
        }
    }
    public List<Propuesta> listarObjetosPorEscritor(Connection cn, int escritorId) throws SQLException {
        String sql = """
            SELECT id, escritor_id, editor_id, titulo_propuesto, resumen, archivo_url,
                   estado, fecha_creacion, fecha_decision
            FROM propuestas
            WHERE escritor_id = ?
            ORDER BY fecha_creacion DESC
            """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, escritorId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Propuesta> lista = new ArrayList<>();

                while (rs.next()) {
                    EstadoPropuesta estado = EstadoPropuesta.valueOf(rs.getString("estado"));

                    Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
                    Timestamp tsDecision = rs.getTimestamp("fecha_decision");

                    Propuesta p = new Propuesta(
                            rs.getInt("id"),
                            rs.getInt("escritor_id"),
                            (Integer) rs.getObject("editor_id"),
                            rs.getString("titulo_propuesto"),
                            rs.getString("resumen"),
                            rs.getString("archivo_url"),
                            estado,
                            tsCreacion != null ? tsCreacion.toLocalDateTime() : null,
                            tsDecision != null ? tsDecision.toLocalDateTime() : null
                    );

                    lista.add(p);
                }

                return lista;
            }
        }
    }

    public List<PropuestaEditorView> listarParaEditor(Connection cn) throws SQLException {
        String sql = """
            SELECT  p.id,
                    u.nombre AS autor,
                    p.titulo_propuesto,
                    p.resumen,
                    p.estado,
                    p.fecha_creacion,
                    p.archivo_url,
                    p.editor_id
            FROM propuestas p
            JOIN usuarios u ON u.id = p.escritor_id
            WHERE p.estado IN ('ENVIADA', 'EN_REVISION')
            ORDER BY p.fecha_creacion ASC
            """;

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<PropuestaEditorView> filas = new ArrayList<>();

            while (rs.next()) {
                EstadoPropuesta estado = EstadoPropuesta.valueOf(rs.getString("estado"));
                Timestamp ts = rs.getTimestamp("fecha_creacion");
                LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : null;

                PropuestaEditorView view = new PropuestaEditorView(
                        rs.getInt("id"),
                        rs.getString("autor"),
                        rs.getString("titulo_propuesto"),
                        rs.getString("resumen"),
                        estado,
                        fecha,
                        rs.getString("archivo_url"),
                        (Integer) rs.getObject("editor_id")
                );
                filas.add(view);
            }

            return filas;
        }
    }

    public List<PropuestaEditorView> listarPorEditor(Connection cn, int editorId) throws SQLException {
        String sql = """
        SELECT  p.id,
                u.nombre AS autor,
                p.titulo_propuesto,
                p.resumen,
                p.estado,
                p.fecha_creacion,
                p.archivo_url,
                p.editor_id
        FROM propuestas p
        JOIN usuarios u ON u.id = p.escritor_id
        WHERE p.editor_id = ?
        ORDER BY p.fecha_creacion DESC
        """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, editorId);

            try (ResultSet rs = ps.executeQuery()) {
                List<PropuestaEditorView> filas = new ArrayList<>();

                while (rs.next()) {
                    EstadoPropuesta estado = EstadoPropuesta.valueOf(rs.getString("estado"));
                    Timestamp ts = rs.getTimestamp("fecha_creacion");
                    LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : null;

                    PropuestaEditorView view = new PropuestaEditorView(
                            rs.getInt("id"),
                            rs.getString("autor"),
                            rs.getString("titulo_propuesto"),
                            rs.getString("resumen"),
                            estado,
                            fecha,
                            rs.getString("archivo_url"),
                            (Integer) rs.getObject("editor_id")
                    );
                    filas.add(view);
                }

                return filas;
            }
        }
    }

    public boolean asignarEditor(Connection cn, int propuestaId, int editorId) throws SQLException {
        String sql = """
            UPDATE propuestas
            SET editor_id = ?,
                estado = CASE WHEN estado = 'ENVIADA' THEN 'EN_REVISION' ELSE estado END
            WHERE id = ? AND editor_id IS NULL
            """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, editorId);
            ps.setInt(2, propuestaId);
            return ps.executeUpdate() > 0;
        }
    }


}

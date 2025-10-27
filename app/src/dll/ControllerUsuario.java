package dll;

import bll.usuarios.Admin;
import bll.usuarios.Editor;
import bll.usuarios.Escritor;
import bll.usuarios.Usuario;
import java.sql.*;

public class ControllerUsuario {

    public boolean emailExiste(Connection cn, String email) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE email = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean insertarEscritor(Connection cn, String nombre, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, rol, biografia) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.setString(4, "ESCRITOR");
            ps.setString(5, "");
            return ps.executeUpdate() > 0;
        }
    }

    public String obtenerPasswordHashPorEmail(Connection cn, String email) throws SQLException {
        String sql = "SELECT password_hash FROM usuarios WHERE email = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return null;
            }
        }
    }


    public Usuario obtenerUsuarioPorEmail(Connection cn, String email) throws SQLException {
        String sql = "SELECT id, nombre, email, rol FROM usuarios WHERE email = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    String id = String.valueOf(rs.getLong("id"));
                    String nombre = rs.getString("nombre");
                    String correo = rs.getString("email");

                    return switch (rol) {
                        case "EDITOR" -> new Editor(id, nombre, correo);
                        case "ADMIN" -> new Admin(id, nombre, correo);
                        default -> new Escritor(id, nombre, correo);
                    };
                }
            }
        }
        return null;
    }

    public boolean insertarUsuario(Connection cn, Usuario usuario, String passwordHash) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, rol) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, passwordHash);
            ps.setString(4, usuario.getRol().name());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarRol(Connection cn, Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET rol = ? WHERE email = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario.getRol().name());
            ps.setString(2, usuario.getEmail());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarUsuario(Connection cn, Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarUsuario(Connection cn, String userId) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public Usuario obtenerUsuarioPorId(Connection cn, String userId) throws SQLException {
        String sql = "SELECT id, nombre, email, rol FROM usuarios WHERE id = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    String id = String.valueOf(rs.getLong("id"));
                    String nombre = rs.getString("nombre");
                    String correo = rs.getString("email");

                    return switch (rol) {
                        case "EDITOR" -> new Editor(id, nombre, correo);
                        case "ADMIN" -> new Admin(id, nombre, correo);
                        default -> new Escritor(id, nombre, correo);
                    };
                }
            }
        }
        return null;
    }

}

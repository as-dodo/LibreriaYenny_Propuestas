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

                    return switch (rol.toUpperCase()) {
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

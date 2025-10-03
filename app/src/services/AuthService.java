package services;

import conexion.Conexion;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

public class AuthService {
    public String iniciarSesion(String email, String password) {
        return "en construcción";
    }

    public String registrarse(String nombre, String email, String password) {
        if (isBlank(nombre) || isBlank(email) || isBlank(password)) {
            return "Error: campos obligatorios vacíos";
        }

        try (Connection cn = Conexion.getInstance().getConnection()) {

            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT 1 FROM usuarios WHERE email = ? LIMIT 1")) {
                ps.setString(1, email.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return "Error: email ya registrado";
                    }
                }
            }

            String hash = sha256(password);

            String sql = "INSERT INTO usuarios (nombre, email, password_hash, rol, biografia) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nombre.trim());
                ps.setString(2, email.trim());
                ps.setString(3, hash);
                ps.setString(4, "ESCRITOR");
                ps.setString(5, "");

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    return " Usuario creado correctamente";
                } else {
                    return " Error: no se pudo crear el usuario";
                }
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String sha256(String s) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            byte[] h = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return s;
        }
    }
}
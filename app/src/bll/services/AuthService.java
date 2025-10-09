package bll.services;

import bll.usuarios.Usuario;
import dll.Conexion;
import dll.ControllerUsuario;
import repository.Validaciones;
import repository.HashUtil;

import java.sql.Connection;

public class AuthService {

    public String iniciarSesion(String email, String password) {

        if (Validaciones.isBlank(email) || Validaciones.isBlank(password)) {
            return "Campos obligatorios vacíos";
        }
        if (!Validaciones.emailValido(email)) {
            return "Email inválido";
        }

        ControllerUsuario ctrl = new ControllerUsuario();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return "No hay conexión a la base de datos";
            }

            if (!ctrl.emailExiste(cn, email.trim())) {
                return "Email no registrado";
            }

            String hashDb = ctrl.obtenerPasswordHashPorEmail(cn, email.trim());
            if (hashDb == null || hashDb.isBlank()) {
                return "Error: no se encontró contraseña para este usuario";
            }

            String hashInput = HashUtil.sha256(password);
            if (hashInput.equals(hashDb)) {
                Usuario usuario = ctrl.obtenerUsuarioPorEmail(cn, email.trim());
                if (usuario != null) {
                    return "¡Bienvenido, " + usuario.getNombre() +
                            "! Sesión iniciada como " + usuario.getRol() + ".";
                }
                return "Sesión iniciada";
            } else {
                return "Credenciales inválidas";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String registrarse(String nombre, String email, String password) {

        if (Validaciones.isBlank(nombre) || Validaciones.isBlank(email) || Validaciones.isBlank(password)) {
            return "Campos obligatorios vacíos";
        }
        if (!Validaciones.emailValido(email)) {
            return "Email inválido";
        }
        if (!Validaciones.passwordMinLen(password, 6)) {
            return "La contraseña debe tener al menos 6 caracteres";
        }

        ControllerUsuario ctrl = new ControllerUsuario();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            if (ctrl.emailExiste(cn, email.trim())) {
                return " Email ya registrado";
            }

            String hash = HashUtil.sha256(password);
            boolean creado = ctrl.insertarEscritor(cn, nombre.trim(), email.trim(), hash);

            if (creado) {
                return "Usuario creado correctamente";
            } else {
                return "Error al crear el usuario";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

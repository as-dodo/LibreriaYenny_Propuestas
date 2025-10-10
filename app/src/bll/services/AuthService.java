package bll.services;

import bll.usuarios.Usuario;
import dll.Conexion;
import dll.ControllerUsuario;
import repository.Validaciones;
import repository.HashUtil;

import java.sql.Connection;

public class AuthService {

    public Usuario autenticar(String email, String password) {
        if (Validaciones.isBlank(email) || Validaciones.isBlank(password)) {
            return null;
        }
        if (!Validaciones.emailValido(email)) {
            return null;
        }

        dll.ControllerUsuario ctrl = new dll.ControllerUsuario();

        try (java.sql.Connection cn = dll.Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return null;
            }

            if (!ctrl.emailExiste(cn, email.trim())) {
                return null;
            }

            String hashDb = ctrl.obtenerPasswordHashPorEmail(cn, email.trim());
            if (hashDb == null || hashDb.isBlank()) {
                return null;
            }

            String hashInput = repository.HashUtil.sha256(password);

            if (hashInput.equals(hashDb)) {
                return ctrl.obtenerUsuarioPorEmail(cn, email.trim());
            }

            return null;

        } catch (Exception e) {
            return null;
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

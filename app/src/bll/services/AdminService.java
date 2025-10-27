package bll.services;

import bll.eventos.AccionEvento;
import bll.usuarios.*;
import dll.Conexion;
import dll.ControllerUsuario;
import java.sql.Connection;
import repository.HashUtil;
import repository.Validaciones;

public class AdminService {

    private final HistoriaService historiaService = new HistoriaService();

    public String crearUsuario(String nombre, String email, String password, String rolStr) {
        if (Validaciones.isBlank(nombre) || Validaciones.isBlank(email) || Validaciones.isBlank(password)) {
            return "Campos obligatorios vacíos";
        }

        if (!Validaciones.emailValido(email)) {
            return "Email inválido";
        }

        if (!Validaciones.passwordMinLen(password, 6)) {
            return "La contraseña debe tener al menos 6 caracteres";
        }

        if (rolStr == null) {
            return "Debe seleccionar un rol";
        }

        Rol rol;
        try {
            rol = Rol.valueOf(rolStr);
        } catch (IllegalArgumentException e) {
            return "Rol inválido";
        }

        Usuario usuario = crearUsuarioSegunRol(nombre.trim(), email.trim(), rol);

        ControllerUsuario ctrl = new ControllerUsuario();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return "No hay conexión a la base de datos";
            }

            if (ctrl.emailExiste(cn, email.trim())) {
                return "Email ya registrado";
            }

            String hash = HashUtil.sha256(password);
            boolean creado = ctrl.insertarUsuario(cn, usuario, hash);

            if (creado) {
                try {
                    historiaService.registrarEvento(null, null, AccionEvento.CREAR_USUARIO, 
                        "Usuario creado: " + email.trim() + " con rol " + rol);
                } catch (Exception ignored) {
                }
                return "Usuario creado correctamente con rol " + rol;
            } else {
                return "Error al crear el usuario";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private Usuario crearUsuarioSegunRol(String nombre, String email, Rol rol) {
        return switch (rol) {
            case EDITOR -> new Editor(null, nombre, email);
            case ADMIN -> new Admin(null, nombre, email);
            case ESCRITOR -> new Escritor(null, nombre, email);
        };
    }

    public String asignarRol(String email, String nuevoRolStr) {
        if (Validaciones.isBlank(email) || !Validaciones.emailValido(email)) {
            return "Email inválido";
        }

        if (nuevoRolStr == null) {
            return "Debe seleccionar un rol";
        }

        Rol nuevoRol;
        try {
            nuevoRol = Rol.valueOf(nuevoRolStr);
        } catch (IllegalArgumentException e) {
            return "Rol inválido";
        }

        ControllerUsuario ctrl = new ControllerUsuario();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return "No hay conexión a la base de datos";
            }

            Usuario usuario = ctrl.obtenerUsuarioPorEmail(cn, email.trim());
            if (usuario == null) {
                return "Usuario no encontrado";
            }

            usuario.setRol(nuevoRol);
            boolean actualizado = ctrl.actualizarRol(cn, usuario);

            if (actualizado) {
                try {
                    historiaService.registrarEvento(null, null, AccionEvento.ASIGNAR_ROL, 
                        "Rol actualizado para " + email.trim() + " a " + nuevoRol);
                } catch (Exception ignored) {
                }
                return "Rol actualizado correctamente a " + nuevoRol;
            } else {
                return "Error al actualizar el rol";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String modificarUsuario(String email, String nuevoNombre, String nuevoEmail) {
        if (Validaciones.isBlank(email)) {
            return "Email actual es obligatorio";
        }

        if (Validaciones.isBlank(nuevoNombre)) {
            return "Nuevo nombre es obligatorio";
        }

        if (Validaciones.isBlank(nuevoEmail) || !Validaciones.emailValido(nuevoEmail)) {
            return "Nuevo email inválido";
        }

        ControllerUsuario ctrl = new ControllerUsuario();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return "No hay conexión a la base de datos";
            }

            Usuario usuario = ctrl.obtenerUsuarioPorEmail(cn, email.trim());
            if (usuario == null) {
                return "Usuario no encontrado";
            }

            if (!email.trim().equals(nuevoEmail.trim())) {
                if (ctrl.emailExiste(cn, nuevoEmail.trim())) {
                    return "El nuevo email ya está registrado";
                }
            }

            usuario.setNombre(nuevoNombre.trim());
            usuario.setEmail(nuevoEmail.trim());

            boolean actualizado = ctrl.actualizarUsuario(cn, usuario);

            if (actualizado) {
                try {
                    historiaService.registrarEvento(null, null, AccionEvento.MODIFICAR_USUARIO, 
                        "Usuario modificado: " + email.trim() + " -> " + nuevoEmail.trim());
                } catch (Exception ignored) {
                }
                return "Usuario modificado correctamente";
            } else {
                return "Error al modificar el usuario";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String eliminarUsuario(String email) {
        if (Validaciones.isBlank(email) || !Validaciones.emailValido(email)) {
            return "Email inválido";
        }

        ControllerUsuario ctrl = new ControllerUsuario();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return "No hay conexión a la base de datos";
            }

            Usuario usuario = ctrl.obtenerUsuarioPorEmail(cn, email.trim());
            if (usuario == null) {
                return "Usuario no encontrado";
            }

            boolean eliminado = ctrl.eliminarUsuario(cn, usuario.getId());

            if (eliminado) {
                try {
                    historiaService.registrarEvento(null, null, AccionEvento.ELIMINAR_USUARIO, 
                        "Usuario eliminado: " + email.trim());
                } catch (Exception ignored) {
                }
                return "Usuario eliminado correctamente";
            } else {
                return "Error al eliminar el usuario";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

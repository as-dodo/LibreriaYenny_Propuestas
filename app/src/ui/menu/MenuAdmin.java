package ui.menu;

import bll.services.AdminService;
import javax.swing.*;

public class MenuAdmin implements Menu {

    private final AdminService adminService = new AdminService();

    @Override
    public void run(){
        String[] opciones = {
                "Crear usuario",
                "Modificar usuario",
                "Eliminar usuario",
                "Asignar rol",
                "Ver reportes",
                "Salir"
        };

        while (true) {
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    "Opciones de Administrador:",
                    "Administrador",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (opcion == JOptionPane.CLOSED_OPTION || opcion == 5) break;

            switch (opcion) {
                case 0 -> crearUsuario();
                case 1 -> modificarUsuario();
                case 2 -> eliminarUsuario();
                case 3 -> asignarRol();
                case 4 -> verReportes();
                default -> {}
            }
        }

    }

    private void crearUsuario() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        if (nombre == null) return;
        
        String email = JOptionPane.showInputDialog("Email:");
        if (email == null) return;
        
        String password = JOptionPane.showInputDialog("Contraseña:");
        if (password == null) return;

        String[] roles = {"ESCRITOR", "EDITOR", "ADMIN"};
        String rol = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona el rol:",
                "Rol",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                roles[0]
        );

        String resultado = adminService.crearUsuario(nombre, email, password, rol);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private void asignarRol() {
        String email = JOptionPane.showInputDialog("Email del usuario:");
        if (email == null) return;

        String[] roles = {"ESCRITOR", "EDITOR", "ADMIN"};
        String nuevoRol = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona el nuevo rol:",
                "Asignar Rol",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                roles[0]
        );

        String resultado = adminService.asignarRol(email, nuevoRol);
        JOptionPane.showMessageDialog(null, resultado);
    }
    private void modificarUsuario() {
        String emailActual = JOptionPane.showInputDialog("Email del usuario a modificar:");
        if (emailActual == null) return;

        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:");
        if (nuevoNombre == null) return;

        String nuevoEmail = JOptionPane.showInputDialog("Nuevo email:");
        if (nuevoEmail == null) return;

        String resultado = adminService.modificarUsuario(emailActual, nuevoNombre, nuevoEmail);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private void eliminarUsuario() {
        String email = JOptionPane.showInputDialog("Email del usuario a eliminar:");
        if (email == null) return;

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Estás seguro de que deseas eliminar el usuario con email: " + email + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = adminService.eliminarUsuario(email);
            JOptionPane.showMessageDialog(null, resultado);
        }
    }

    private void verReportes() {
        MenuReporte menuReporte = new MenuReporte();
        menuReporte.mostrarMenu();
    }
}

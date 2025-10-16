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

            if (opcion == JOptionPane.CLOSED_OPTION || opcion == 4) break;

            switch (opcion) {
                case 0 -> crearUsuario();
                case 1 -> JOptionPane.showMessageDialog(null, "Modificar usuario");
                case 2 -> JOptionPane.showMessageDialog(null, "Eliminar usuario");
                case 3 -> asignarRol();
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
}

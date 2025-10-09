package ui.menu;

import javax.swing.*;
import bll.services.AuthService;

public class MenuPrincipal implements Menu {
    @Override
    public void run() {
        String[] opciones = {
                "Iniciar sesión",
                "Registrarse",
                "Salir"
        };
        AuthService auth = new AuthService();

        while (true) {
            int opcion = JOptionPane.showOptionDialog (
                    null,
                    "Elige una opción:",
                    "Sistema de Propuestas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
            if (opcion == 2 || opcion == JOptionPane.CLOSED_OPTION) break;

            switch (opcion) {
                case 0 -> {
                    String email = JOptionPane.showInputDialog("Email:");
                    if (email == null) break;
                    String pass  = JOptionPane.showInputDialog("Contraseña:");
                    if (pass == null) break;
                    String msg = new bll.services.AuthService().iniciarSesion(email, pass);
                    JOptionPane.showMessageDialog(null, msg);
                }
                case 1 -> {
                    String nombre = JOptionPane.showInputDialog("Nombre:");
                    if (nombre == null) break;
                    String email  = JOptionPane.showInputDialog("Email:");
                    if (email == null) break;
                    String pass   = JOptionPane.showInputDialog("Contraseña:");
                    if (pass == null) break;
                    String resultado = auth.registrarse(nombre, email, pass);
                    JOptionPane.showMessageDialog(null, resultado);;
                }
                default -> {}
            }
        }
    }
}

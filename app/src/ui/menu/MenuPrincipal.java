package ui.menu;

import javax.swing.*;
import bll.services.AuthService;
import bll.usuarios.Escritor;
import bll.usuarios.Usuario;

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
                    LoginDialog dlg = new LoginDialog(null);
                    dlg.setVisible(true);

                    String email = dlg.getEmail();
                    String pass = dlg.getPassword();

                    if (email == null || pass == null) break;

                    Usuario usuario = auth.autenticar(email, pass);
                    if (usuario == null) {
                        JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos.");
                        continue;
                    }

                    JOptionPane.showMessageDialog(null,
                            "¡Bienvenido, " + usuario.getNombre() + "! Sesión iniciada como " + usuario.getRol() + ".");

                    switch (usuario.getRol()) {
                        case ESCRITOR -> new MenuEscritor((Escritor) usuario).run();
                        case EDITOR -> new MenuEditor(usuario).run();
                        case ADMIN -> new MenuAdmin().run();
                        default -> {
                        }
                    }
                }
                case 1 -> {
                    String nombre = JOptionPane.showInputDialog("Nombre:");
                    if (nombre == null) break;
                    String email  = JOptionPane.showInputDialog("Email:");
                    if (email == null) break;
                    String pass   = JOptionPane.showInputDialog("Contraseña:");
                    if (pass == null) break;
                    String resultado = auth.registrarse(nombre, email, pass);
                    JOptionPane.showMessageDialog(null, resultado);
                }
                default -> {}
            }
        }
    }
}

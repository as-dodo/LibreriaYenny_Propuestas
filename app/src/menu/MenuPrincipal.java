package menu;

import javax.swing.*;

public class MenuPrincipal implements Menu {
    @Override
    public void run() {
        String[] opciones = {
                "Iniciar sesión",
                "Registrarse",
                "Salir"
        };

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
                case 0 -> JOptionPane.showMessageDialog(null, "Iniciar sesión");
                case 1 -> JOptionPane.showMessageDialog(null, "Registrarse");
                default -> {}
            }
        }
    }
}

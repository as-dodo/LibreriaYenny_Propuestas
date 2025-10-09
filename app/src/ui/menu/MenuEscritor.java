package ui.menu;

import javax.swing.*;

public class MenuEscritor implements Menu {
    @Override
    public void run() {
        String[] opciones = {
                "Enviar propuesta",
                "Ver mis propuestas",
                "Salir"
        };

        while (true) {
            int opcion = JOptionPane.showOptionDialog (
                    null,
                    "Opciones de Escritor:",
                    "Escritor",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
            if (opcion == 2 || opcion == JOptionPane.CLOSED_OPTION) break;

            switch (opcion) {
                case 0 -> JOptionPane.showMessageDialog(null, "Enviar propuesta");
                case 1 -> JOptionPane.showMessageDialog(null, "Ver mis propuestas");
                default -> {}
            }
        }
    }
}

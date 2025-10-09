package ui.menu;

import javax.swing.*;

public class MenuEditor implements Menu {
    @Override
    public void run() {
        String[] opciones = {
                "Ver bandeja de propuestas",
                "Revisar propuesta",
                "Definir condiciones de publicación",
                "Crear título desde propuesta",
                "Actualizar estado de comercialización",
                "Salir"
        };

        while (true) {
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    "Opciones de Editor:",
                    "Editor",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (opcion == JOptionPane.CLOSED_OPTION || opcion == 5) break;

            switch (opcion) {
                case 0 -> JOptionPane.showMessageDialog(null, "Ver bandeja de propuestas");
                case 1 -> JOptionPane.showMessageDialog(null, "Revisar/Decidir propuesta");
                case 2 -> JOptionPane.showMessageDialog(null, "Definir condiciones de publicación");
                case 3 -> JOptionPane.showMessageDialog(null, "Crear título desde propuesta");
                case 4 -> JOptionPane.showMessageDialog(null, "Actualizar estado de comercialización");
                default -> {}
            }
        }
    }
}

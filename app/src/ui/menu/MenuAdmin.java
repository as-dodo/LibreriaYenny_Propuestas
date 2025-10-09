package ui.menu;

import javax.swing.*;

public class MenuAdmin implements Menu {
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
                case 0 -> JOptionPane.showMessageDialog(null, "Crear usuario");
                case 1 -> JOptionPane.showMessageDialog(null, "Modificar usuario");
                case 2 -> JOptionPane.showMessageDialog(null, "Eliminar usuario");
                case 3 -> JOptionPane.showMessageDialog(null, "Asignar rol");
                default -> {}
            }
        }

    }
}

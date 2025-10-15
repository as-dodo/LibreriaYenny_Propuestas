package ui.menu;

import bll.services.PropuestaService;
import bll.usuarios.Escritor;

import javax.swing.*;

public class MenuEscritor implements Menu {

    private final Escritor escritor;
    private final PropuestaService service = new PropuestaService();

    public MenuEscritor(Escritor escritor) {
        this.escritor = escritor;
    }

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
                case 0 -> enviarPropuesta();
                case 1 -> verMisPropuestas();
                default -> {}
            }
        }
    }

    private void enviarPropuesta() {
        String titulo  = JOptionPane.showInputDialog("Título propuesto:");
        if (titulo == null) return;
        String resumen = JOptionPane.showInputDialog("Resumen (breve):");
        if (resumen == null) return;
        String enlace  = JOptionPane.showInputDialog("Enlace al archivo (opcional):");

        String msg = service.enviarPropuesta(escritor.getId(), titulo, resumen, enlace);
        JOptionPane.showMessageDialog(null, msg);
    }

    private void verMisPropuestas() {
        String listado = service.listarPorEscritor(escritor.getId());
        JTextArea ta = new JTextArea(listado, 15, 60);
        ta.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(ta), "Mis propuestas", JOptionPane.INFORMATION_MESSAGE);
    }
}


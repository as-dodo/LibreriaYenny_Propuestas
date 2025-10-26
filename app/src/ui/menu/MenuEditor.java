package ui.menu;

import bll.services.PropuestaService;
import bll.services.TituloService;
import bll.usuarios.Usuario;
import javax.swing.*;

public class MenuEditor implements Menu {

    private final PropuestaService service = new PropuestaService();
    private final TituloService tituloService = new TituloService();
    private final Usuario editorActual;

    public MenuEditor(Usuario editorActual) {
        this.editorActual = editorActual;
    }

    @Override
    public void run() {
        String[] opciones = {
                "Ver bandeja de propuestas",
                "Revisar propuesta",
                "Definir condiciones de publicación",
                "Crear título desde propuesta aprobada",
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

            if (opcion == 4 || opcion == JOptionPane.CLOSED_OPTION) break;

            switch (opcion) {
                case 0 -> verBandeja();
                case 1 -> revisarPropuesta();
                case 2 -> definirCondiciones();
                case 3 -> crearTitulo();
                default -> {}
            }
        }
    }

    private void verBandeja() {
        String listado = service.listarBandeja();
        if (listado == null || listado.isBlank()) listado = "Bandeja vacía.";
        JOptionPane.showMessageDialog(null, listado, "Bandeja de propuestas", JOptionPane.INFORMATION_MESSAGE);
    }


    private void revisarPropuesta() {
        String id = JOptionPane.showInputDialog("ID de la propuesta:");
        if (id == null || id.isBlank()) return;

        String[] acciones = { "Ver propuesta", "Aprobar", "Rechazar", "Agregar comentario", "Cancelar" };
        int acc = JOptionPane.showOptionDialog(
                null,
                "Elegir acción para la propuesta #" + id,
                "Revisar propuesta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                acciones,
                acciones[0]
        );

        switch (acc) {
            case 0 -> {
                String detalle = service.obtenerDetalle(id);
                if (detalle == null || detalle.startsWith("Error")) {
                    JOptionPane.showMessageDialog(null, "No se encontró la propuesta #" + id);
                } else {
                    JOptionPane.showMessageDialog(null, detalle, "Propuesta #" + id, JOptionPane.INFORMATION_MESSAGE);
                }
            }
            case 1 -> {
                JOptionPane.showMessageDialog(null, service.aprobar(id));
            }
            case 2 -> {
                JOptionPane.showMessageDialog(null, service.rechazar(id));
            }
            case 3 -> {
                String comentario = JOptionPane.showInputDialog("Comentario:");
                if (comentario == null || comentario.isBlank()) return;

                int editorId = Integer.parseInt(editorActual.getId().trim());
                String msg = service.agregarComentario(id, editorId, comentario);
                JOptionPane.showMessageDialog(null, msg);
            }
            default -> {}
        }
    }

    private void definirCondiciones() {
        String propuestaId = JOptionPane.showInputDialog("ID de la propuesta aprobada:");
        if (propuestaId == null) return;

        String tirada = JOptionPane.showInputDialog("Tirada inicial:");
        if (tirada == null) return;

        String porcentaje = JOptionPane.showInputDialog("Porcentaje de ganancias para el autor (0-100):");
        if (porcentaje == null) return;

        String observaciones = JOptionPane.showInputDialog("Observaciones (opcional):");

        String resultado = tituloService.definirCondiciones(propuestaId, tirada, porcentaje, observaciones);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private void crearTitulo() {
        String propuestaId = JOptionPane.showInputDialog("ID de la propuesta aprobada:");
        if (propuestaId == null) return;

        String titulo = JOptionPane.showInputDialog("Título del libro:");
        if (titulo == null) return;

        String resultado = tituloService.crearTitulo(propuestaId, titulo);
        JOptionPane.showMessageDialog(null, resultado);
    }
}

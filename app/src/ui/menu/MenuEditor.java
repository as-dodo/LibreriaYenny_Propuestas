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
                "Crear título",
                "Ver títulos",
                "Transferir título a Marketing/Ventas",
                "Actualizar estado de comercialización",
                "Ver reportes",
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

            if (opcion == 8 || opcion == JOptionPane.CLOSED_OPTION) break;

            switch (opcion) {
                case 0 -> verBandeja();
                case 1 -> revisarPropuesta();
                case 2 -> definirCondiciones();
                case 3 -> crearTitulo();
                case 4 -> verTitulos();
                case 5 -> transferirAMarketing();
                case 6 -> actualizarEstadoComercializacion();
                case 7 -> verReportes();
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

    private void verTitulos() {
        String listado = tituloService.listarTitulos();
        JTextArea ta = new JTextArea(listado, 15, 70);
        ta.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(ta), "Títulos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void transferirAMarketing() {
        String tituloId = JOptionPane.showInputDialog("ID del título a transferir:");
        if (tituloId == null) return;

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirmar transferencia del título #" + tituloId + " a Marketing/Ventas?",
                "Confirmar transferencia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = tituloService.transferirAMarketing(tituloId);
            JOptionPane.showMessageDialog(null, resultado);
        }
    }

    private void actualizarEstadoComercializacion() {
        String tituloId = JOptionPane.showInputDialog("ID del título:");
        if (tituloId == null) return;

        String[] estados = {
                "EN_PREPARACION",
                "EN_PROMOCION",
                "DISPONIBLE",
                "AGOTADO"
        };

        String nuevoEstado = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona el nuevo estado de comercialización:",
                "Actualizar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                estados,
                estados[0]
        );

        if (nuevoEstado != null) {
            String resultado = tituloService.actualizarEstadoComercializacion(tituloId, nuevoEstado);
            JOptionPane.showMessageDialog(null, resultado);
        }
    }

    private void verReportes() {
        MenuReporte menuReporte = new MenuReporte();
        menuReporte.mostrarMenu();
    }
}

package bll.services;

import bll.eventos.AccionEvento;
import bll.eventos.HistorialEvento;
import dll.Conexion;
import dll.ControllerHistorial;
import java.sql.Connection;

public class HistoriaService {

    public void registrarEvento(Integer propuestaId, Integer usuarioId, AccionEvento accion, String descripcion) {
        HistorialEvento evento = new HistorialEvento(propuestaId, usuarioId, accion, descripcion);
        ControllerHistorial ctrl = new ControllerHistorial();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn != null) {
                ctrl.registrarEvento(cn, evento);
            }
        } catch (Exception e) {
            System.err.println("Error al registrar evento: " + e.getMessage());
        }
    }

    public String listarEventosPorPropuesta(String propuestaId) {
        if (propuestaId == null || propuestaId.isBlank()) {
            return "ID de propuesta inválido";
        }

        try {
            int idPropuesta = Integer.parseInt(propuestaId.trim());
            ControllerHistorial ctrl = new ControllerHistorial();

            try (Connection cn = Conexion.getInstance().getConnection()) {
                if (cn == null) return "No hay conexión a la base de datos";

                java.util.List<String> eventos = ctrl.listarEventosPorPropuesta(cn, idPropuesta);
                if (eventos.isEmpty()) {
                    return "No hay eventos registrados para esta propuesta";
                }

                return String.join("\n", eventos);
            }
        } catch (NumberFormatException e) {
            return "ID de propuesta inválido";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

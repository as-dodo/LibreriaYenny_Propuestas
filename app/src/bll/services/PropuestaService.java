package bll.services;

import bll.eventos.AccionEvento;
import bll.propuestas.EstadoPropuesta;
import bll.propuestas.Propuesta;
import dll.Conexion;
import dll.ControllerPropuesta;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import repository.Validaciones;

public class PropuestaService {

    private final HistoriaService historiaService = new HistoriaService();

    public String enviarPropuesta(String escritorId, String titulo, String resumen, String enlace) {

        if (Validaciones.isBlank(escritorId) || !Validaciones.esNumero(escritorId)) return "Escritor inválido";
        if (Validaciones.isBlank(titulo))  return "Título obligatorio";
        if (Validaciones.isBlank(resumen)) return "Resumen obligatorio";
        if (enlace != null && enlace.length() > 500) return "Enlace demasiado largo";

        int idEscritor = Integer.parseInt(escritorId.trim());
        String archivoUrl = Validaciones.linkOrNull(enlace);

        Propuesta propuesta = new Propuesta(
                idEscritor,
                titulo.trim(),
                resumen.trim(),
                archivoUrl
        );

        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            boolean resultado = ctrl.insertarPropuesta(cn, propuesta);

            if (resultado) {
                try {
                    historiaService.registrarEvento(null, idEscritor, AccionEvento.ENVIAR_PROPUESTA,
                            "Propuesta enviada: " + titulo.trim());
                } catch (Exception ignored) {
                }
                return "Propuesta enviada";
            }

            return "No se pudo enviar la propuesta";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listarPorEscritor(String escritorId) {
        if (Validaciones.isBlank(escritorId) || !Validaciones.esNumero(escritorId)) return "Escritor inválido";
        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            List<String> filas = ctrl.listarPorEscritor(cn, Integer.parseInt(escritorId));
            if (filas.isEmpty()) return "No tenés propuestas todavía";
            return String.join("\n", filas);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listarBandeja() {
        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            List<String> filas = ctrl.listarBandeja(cn);
            if (filas.isEmpty()) return "Bandeja vacía";
            return filas.stream().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String aprobar(String propuestaId) { return decidir(propuestaId, EstadoPropuesta.APROBADA, null); }
    public String aprobar(String propuestaId, Integer editorId) { return decidir(propuestaId, EstadoPropuesta.APROBADA, editorId); }
    public String rechazar(String propuestaId) { return decidir(propuestaId, EstadoPropuesta.RECHAZADA, null); }
    public String rechazar(String propuestaId, Integer editorId) { return decidir(propuestaId, EstadoPropuesta.RECHAZADA, editorId); }

    private String decidir(String propuestaId, EstadoPropuesta estado, Integer editorId) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) return "ID inválido";

        int idPropuesta = Integer.parseInt(propuestaId.trim());

        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            Propuesta propuesta = ctrl.obtenerPorId(cn, idPropuesta);
            if (propuesta == null) {
                return "Propuesta no encontrada";
            }

            propuesta.setEstado(estado);
            boolean ok = ctrl.actualizarEstado(cn, propuesta);

            if (ok) {
                try {
                    AccionEvento accion = estado == EstadoPropuesta.APROBADA ?
                            AccionEvento.APROBAR_PROPUESTA : AccionEvento.RECHAZAR_PROPUESTA;
                    historiaService.registrarEvento(idPropuesta, editorId, accion,
                            "Propuesta " + estado.name().toLowerCase());
                } catch (Exception ignored) {
                }
            }

            return ok ? ("Propuesta " + estado.name().toLowerCase()) : "No se pudo actualizar el estado";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String obtenerDetalle(String propuestaId) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) return null;
        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            return ctrl.obtenerDetalle(cn, Integer.parseInt(propuestaId));
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String agregarComentario(String propuestaId, int usuarioId, String comentario) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) return "ID inválido";
        if (usuarioId <= 0) return "Usuario inválido";
        if (Validaciones.isBlank(comentario)) return "Comentario vacío";

        int idPropuesta = Integer.parseInt(propuestaId.trim());

        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            boolean ok = ctrl.agregarComentario(cn, idPropuesta, usuarioId, comentario);

            if (ok) {
                try {
                    historiaService.registrarEvento(idPropuesta, usuarioId, AccionEvento.AGREGAR_COMENTARIO,
                            "Comentario agregado");
                } catch (Exception ignored) {
                }
            }

            return ok ? "Comentario agregado" : "No se pudo agregar el comentario";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<Propuesta> obtenerPorEscritor(String escritorId) {
        List<Propuesta> propuestas = new ArrayList<>();

        if (Validaciones.isBlank(escritorId) || !Validaciones.esNumero(escritorId))
            return propuestas;

        int id = Integer.parseInt(escritorId.trim());
        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return propuestas;

            return ctrl.listarObjetosPorEscritor(cn, id);

        } catch (Exception e) {
            e.printStackTrace();
            return propuestas;
        }
    }


}


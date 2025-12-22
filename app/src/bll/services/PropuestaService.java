package bll.services;

import bll.eventos.AccionEvento;
import bll.propuestas.EstadoPropuesta;
import bll.propuestas.Propuesta;
import bll.views.PropuestaEditorView;
import dll.Conexion;
import dll.ControllerPropuesta;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import repository.FileStorage;
import repository.Validaciones;

public class PropuestaService {

    private final HistoriaService historiaService = new HistoriaService();

    public String enviarPropuesta(String escritorId, String titulo, String resumen, String rutaArchivo) {

        if (Validaciones.isBlank(escritorId) || !Validaciones.esNumero(escritorId)) return "Escritor invalido";
        if (Validaciones.isBlank(titulo))  return "Titulo obligatorio";
        if (Validaciones.isBlank(resumen)) return "Resumen obligatorio";
        if (rutaArchivo != null && rutaArchivo.length() > 500) return "Ruta demasiado larga";

        int idEscritor = Integer.parseInt(escritorId.trim());
        String archivoUrl = null;
        try {
            if (!Validaciones.isBlank(rutaArchivo)) {
                archivoUrl = FileStorage.saveFile(rutaArchivo.trim());
            }
        } catch (Exception e) {
            return "No se pudo guardar el archivo: " + e.getMessage();
        }

        Propuesta propuesta = new Propuesta(
                idEscritor,
                titulo.trim(),
                resumen.trim(),
                archivoUrl
        );

        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

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
        if (Validaciones.isBlank(escritorId) || !Validaciones.esNumero(escritorId)) return "Escritor invalido";
        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";
            List<String> filas = ctrl.listarPorEscritor(cn, Integer.parseInt(escritorId));
            if (filas.isEmpty()) return "No tenes propuestas todavia";
            return String.join("\n", filas);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listarBandeja() {
        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";
            List<String> filas = ctrl.listarBandeja(cn);
            if (filas.isEmpty()) return "Bandeja vacia";
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
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) return "ID invalido";

        int idPropuesta = Integer.parseInt(propuestaId.trim());

        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

            Propuesta propuesta = ctrl.obtenerPorId(cn, idPropuesta);
            if (propuesta == null) {
                return "Propuesta no encontrada";
            }

            propuesta.setEstado(estado);
            propuesta.setEditorId(editorId);
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
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) return "ID invalido";
        if (usuarioId <= 0) return "Usuario invalido";
        if (Validaciones.isBlank(comentario)) return "Comentario vacio";

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

    public List<PropuestaEditorView> obtenerPorEditor() {
        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return Collections.emptyList();
            }

            return ctrl.listarParaEditor(cn);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<PropuestaEditorView> obtenerPorEditor(String editorId) {
        List<PropuestaEditorView> propuestas = new ArrayList<>();

        if (Validaciones.isBlank(editorId) || !Validaciones.esNumero(editorId)) {
            return propuestas;
        }

        int id = Integer.parseInt(editorId.trim());
        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) {
                return propuestas;
            }

            return ctrl.listarPorEditor(cn, id);
        } catch (Exception e) {
            e.printStackTrace();
            return propuestas;
        }
    }

    public String asignarAEditor(String propuestaId, int editorId) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) {
            return "ID invalido";
        }
        if (editorId <= 0) return "Editor invalido";

        int idPropuesta = Integer.parseInt(propuestaId.trim());
        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

            boolean asignado = ctrl.asignarEditor(cn, idPropuesta, editorId);
            return asignado ? "Propuesta asignada al editor" : "La propuesta ya tiene editor";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}

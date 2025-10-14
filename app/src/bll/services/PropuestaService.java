package bll.services;

import bll.propuestas.EstadoPropuesta;
import dll.Conexion;
import dll.ControllerPropuesta;
import repository.Validaciones;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class PropuestaService {

    public String enviarPropuesta(String escritorId, String titulo, String resumen, String enlace) {

        if (Validaciones.isBlank(escritorId) || !Validaciones.esNumero(escritorId)) return "Escritor inválido";
        if (Validaciones.isBlank(titulo))  return "Título obligatorio";
        if (Validaciones.isBlank(resumen)) return "Resumen obligatorio";
        if (enlace != null && enlace.length() > 500) return "Enlace demasiado largo";

        ControllerPropuesta ctrl = new ControllerPropuesta();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            boolean resultado = ctrl.insertarPropuesta(
                    cn,
                   Integer.parseInt(escritorId),
                    titulo.trim(),
                    resumen.trim(),
                    Validaciones.linkOrNull(enlace)
            );

            if (resultado) {
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

    public String aprobar(String propuestaId) { return decidir(propuestaId, EstadoPropuesta.APROBADA); }
    public String rechazar(String propuestaId) { return decidir(propuestaId, EstadoPropuesta.RECHAZADA); }

    private String decidir(String propuestaId, EstadoPropuesta estado) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) return "ID inválido";
        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            boolean ok = ctrl.actualizarEstado(cn, Integer.parseInt(propuestaId), estado);
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

        ControllerPropuesta ctrl = new ControllerPropuesta();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            boolean ok = ctrl.agregarComentario(cn, Integer.parseInt(propuestaId), usuarioId, comentario);
            return ok ? "Comentario agregado" : "No se pudo agregar el comentario";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}


package bll.services;

import bll.titulos.CondicionesPublicacion;
import bll.titulos.EstadoComercializacion;
import bll.titulos.Titulo;
import dll.Conexion;
import dll.ControllerTitulo;
import java.sql.Connection;
import repository.Validaciones;

public class TituloService {

    public String definirCondiciones(String propuestaId, String tiradaStr, String porcentajeStr, String observaciones) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) {
            return "ID de propuesta inválido";
        }

        if (Validaciones.isBlank(tiradaStr) || !Validaciones.esNumero(tiradaStr)) {
            return "Tirada inicial debe ser un número";
        }

        int tirada = Integer.parseInt(tiradaStr.trim());
        if (tirada <= 0) {
            return "Tirada inicial debe ser mayor a 0";
        }

        if (Validaciones.isBlank(porcentajeStr)) {
            return "Porcentaje de ganancias es obligatorio";
        }

        double porcentaje;
        try {
            porcentaje = Double.parseDouble(porcentajeStr.trim());
            if (porcentaje < 0 || porcentaje > 100) {
                return "Porcentaje debe estar entre 0 y 100";
            }
        } catch (NumberFormatException e) {
            return "Porcentaje inválido";
        }

        int propId = Integer.parseInt(propuestaId.trim());
        String obs = Validaciones.linkOrNull(observaciones);
        
        CondicionesPublicacion condiciones = new CondicionesPublicacion(propId, tirada, porcentaje, obs);
        
        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            String estado = ctrl.obtenerEstadoPropuesta(cn, propId);
            if (estado == null) {
                return "Propuesta no encontrada";
            }

            if (!estado.equals("APROBADA")) {
                return "Solo se pueden definir condiciones para propuestas aprobadas";
            }

            if (ctrl.yaExistenCondiciones(cn, propId)) {
                return "Ya existen condiciones definidas para esta propuesta";
            }

            boolean creado = ctrl.insertarCondiciones(cn, condiciones);

            if (creado) {
                return "Condiciones de publicación definidas correctamente";
            } else {
                return "Error al definir condiciones";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String crearTitulo(String propuestaId, String tituloStr) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) {
            return "ID de propuesta inválido";
        }

        if (Validaciones.isBlank(tituloStr)) {
            return "El título es obligatorio";
        }

        int propId = Integer.parseInt(propuestaId.trim());
        
        Titulo titulo = new Titulo(propId, tituloStr.trim(), EstadoComercializacion.EN_PREPARACION);
        
        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            String estado = ctrl.obtenerEstadoPropuesta(cn, propId);
            if (estado == null) {
                return "Propuesta no encontrada";
            }

            if (!estado.equals("APROBADA")) {
                return "Solo se pueden crear títulos desde propuestas aprobadas";
            }

            if (ctrl.yaExisteTitulo(cn, propId)) {
                return "Ya existe un título creado para esta propuesta";
            }

            boolean creado = ctrl.insertarTitulo(cn, titulo);

            if (creado) {
                return "Título creado correctamente";
            } else {
                return "Error al crear el título";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

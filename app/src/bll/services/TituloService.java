package bll.services;

import bll.eventos.AccionEvento;
import bll.titulos.CondicionesPublicacion;
import bll.titulos.EstadoComercializacion;
import bll.titulos.Titulo;
import dll.Conexion;
import dll.ControllerTitulo;
import java.sql.Connection;
import repository.Validaciones;

public class TituloService {

    private final HistoriaService historiaService = new HistoriaService();

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
                try {
                    historiaService.registrarEvento(propId, null, AccionEvento.DEFINIR_CONDICIONES, 
                        "Condiciones definidas: tirada " + tirada + ", porcentaje " + porcentaje + "%");
                } catch (Exception ignored) {
                }
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
                try {
                    historiaService.registrarEvento(propId, null, AccionEvento.CREAR_TITULO, 
                        "Título creado: " + tituloStr.trim());
                } catch (Exception ignored) {
                }
                return "Título creado correctamente";
            } else {
                return "Error al crear el título";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String transferirAMarketing(String tituloId) {
        if (Validaciones.isBlank(tituloId) || !Validaciones.esNumero(tituloId)) {
            return "ID de título inválido";
        }

        int idTitulo = Integer.parseInt(tituloId.trim());
        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            Titulo titulo = ctrl.obtenerTituloPorId(cn, idTitulo);
            if (titulo == null) {
                return "Título no encontrado";
            }

            if (titulo.getEstadoComercializacion() != EstadoComercializacion.EN_PREPARACION) {
                return "Solo se pueden transferir títulos en preparación";
            }

            titulo.setEstadoComercializacion(EstadoComercializacion.EN_PROMOCION);
            boolean actualizado = ctrl.actualizarEstadoComercializacion(cn, titulo);

            if (actualizado) {
                try {
                    historiaService.registrarEvento(titulo.getPropuestaId(), null, AccionEvento.TRANSFERIR_MARKETING, 
                        "Título transferido a Marketing/Ventas");
                } catch (Exception ignored) {
                }
                return "Título transferido a Marketing/Ventas correctamente";
            } else {
                return "Error al transferir el título";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String actualizarEstadoComercializacion(String tituloId, String nuevoEstadoStr) {
        if (Validaciones.isBlank(tituloId) || !Validaciones.esNumero(tituloId)) {
            return "ID de título inválido";
        }

        if (nuevoEstadoStr == null) {
            return "Debe seleccionar un estado";
        }

        EstadoComercializacion nuevoEstado;
        try {
            nuevoEstado = EstadoComercializacion.valueOf(nuevoEstadoStr);
        } catch (IllegalArgumentException e) {
            return "Estado inválido";
        }

        int idTitulo = Integer.parseInt(tituloId.trim());
        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";

            Titulo titulo = ctrl.obtenerTituloPorId(cn, idTitulo);
            if (titulo == null) {
                return "Título no encontrado";
            }

            titulo.setEstadoComercializacion(nuevoEstado);
            boolean actualizado = ctrl.actualizarEstadoComercializacion(cn, titulo);

            if (actualizado) {
                try {
                    historiaService.registrarEvento(titulo.getPropuestaId(), null, 
                        AccionEvento.ACTUALIZAR_ESTADO_COMERCIALIZACION, 
                        "Estado actualizado a " + nuevoEstado.name());
                } catch (Exception ignored) {
                }
                return "Estado de comercialización actualizado correctamente a " + nuevoEstado.name();
            } else {
                return "Error al actualizar el estado";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listarTitulos() {
        ControllerTitulo ctrl = new ControllerTitulo();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            
            java.util.List<String> filas = ctrl.listarTitulos(cn);
            if (filas.isEmpty()) return "No hay títulos registrados";
            
            return String.join("\n", filas);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

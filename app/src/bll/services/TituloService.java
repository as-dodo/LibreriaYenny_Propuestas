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

    public String definirCondiciones(String propuestaId, String tiradaStr, String porcentajeStr, String precioStr, String observaciones) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) {
            return "ID de propuesta invalido";
        }

        if (Validaciones.isBlank(tiradaStr) || !Validaciones.esNumero(tiradaStr)) {
            return "Tirada inicial debe ser un numero";
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
            return "Porcentaje invalido";
        }

        if (Validaciones.isBlank(precioStr)) {
            return "Precio por ejemplar es obligatorio";
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr.trim());
            if (precio <= 0) {
                return "Precio debe ser mayor a 0";
            }
        } catch (NumberFormatException e) {
            return "Precio invalido";
        }

        int propId = Integer.parseInt(propuestaId.trim());
        String obs = Validaciones.linkOrNull(observaciones);

        CondicionesPublicacion condiciones = new CondicionesPublicacion(propId, tirada, porcentaje, precio, obs);

        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

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
                        "Condiciones definidas: tirada " + tirada + ", porcentaje " + porcentaje + "%, precio " + precio);
                } catch (Exception ignored) {
                }
                return "Condiciones de publicacion definidas correctamente";
            } else {
                return "Error al definir condiciones";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String crearTitulo(String propuestaId, String tituloStr) {
        if (Validaciones.isBlank(propuestaId) || !Validaciones.esNumero(propuestaId)) {
            return "ID de propuesta invalido";
        }

        if (Validaciones.isBlank(tituloStr)) {
            return "El titulo es obligatorio";
        }

        int propId = Integer.parseInt(propuestaId.trim());

        Titulo titulo = new Titulo(propId, tituloStr.trim(), EstadoComercializacion.EN_PREPARACION);

        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

            String estado = ctrl.obtenerEstadoPropuesta(cn, propId);
            if (estado == null) {
                return "Propuesta no encontrada";
            }

            if (!estado.equals("APROBADA")) {
                return "Solo se pueden crear titulos desde propuestas aprobadas";
            }

            if (ctrl.yaExisteTitulo(cn, propId)) {
                return "Ya existe un titulo creado para esta propuesta";
            }

            boolean creado = ctrl.insertarTitulo(cn, titulo);

            if (creado) {
                try {
                    historiaService.registrarEvento(propId, null, AccionEvento.CREAR_TITULO,
                        "Titulo creado: " + tituloStr.trim());
                } catch (Exception ignored) {
                }
                return "Titulo creado correctamente";
            } else {
                return "Error al crear el titulo";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String transferirAMarketing(String tituloId) {
        if (Validaciones.isBlank(tituloId) || !Validaciones.esNumero(tituloId)) {
            return "ID de titulo invalido";
        }

        int idTitulo = Integer.parseInt(tituloId.trim());
        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

            Titulo titulo = ctrl.obtenerTituloPorId(cn, idTitulo);
            if (titulo == null) {
                return "Titulo no encontrado";
            }

            if (titulo.getEstadoComercializacion() != EstadoComercializacion.EN_PREPARACION) {
                return "Solo se pueden transferir titulos en preparacion";
            }

            titulo.setEstadoComercializacion(EstadoComercializacion.EN_PROMOCION);
            boolean actualizado = ctrl.actualizarEstadoComercializacion(cn, titulo);

            if (actualizado) {
                try {
                    historiaService.registrarEvento(titulo.getPropuestaId(), null, AccionEvento.TRANSFERIR_MARKETING,
                        "Titulo transferido a Marketing/Ventas");
                } catch (Exception ignored) {
                }
                return "Titulo transferido a Marketing/Ventas correctamente";
            } else {
                return "Error al transferir el titulo";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String actualizarEstadoComercializacion(String tituloId, String nuevoEstadoStr) {
        if (Validaciones.isBlank(tituloId) || !Validaciones.esNumero(tituloId)) {
            return "ID de titulo invalido";
        }

        if (nuevoEstadoStr == null) {
            return "Debe seleccionar un estado";
        }

        EstadoComercializacion nuevoEstado;
        try {
            nuevoEstado = EstadoComercializacion.valueOf(nuevoEstadoStr);
        } catch (IllegalArgumentException e) {
            return "Estado invalido";
        }

        int idTitulo = Integer.parseInt(tituloId.trim());
        ControllerTitulo ctrl = new ControllerTitulo();

        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexion a la base de datos";

            Titulo titulo = ctrl.obtenerTituloPorId(cn, idTitulo);
            if (titulo == null) {
                return "Titulo no encontrado";
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
                return "Estado de comercializacion actualizado correctamente a " + nuevoEstado.name();
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
            if (cn == null) return "No hay conexion a la base de datos";

            java.util.List<String> filas = ctrl.listarTitulos(cn);
            if (filas.isEmpty()) return "No hay titulos registrados";

            return String.join("\n", filas);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public java.util.List<Titulo> obtenerTodosTitulos() {
        ControllerTitulo ctrl = new ControllerTitulo();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return new java.util.ArrayList<>();
            return ctrl.obtenerTodos(cn);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }
}

package bll.services;

import dll.Conexion;
import dll.ControllerReporte;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ReporteService {

    public String obtenerEstadisticasGenerales() {
        ControllerReporte ctrl = new ControllerReporte();
        
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            
            Map<String, Integer> statsUsuarios = ctrl.obtenerEstadisticasUsuarios(cn);
            Map<String, Integer> statsPropuestas = ctrl.obtenerEstadisticasPropuestas(cn);
            Map<String, Integer> statsTitulos = ctrl.obtenerEstadisticasTitulos(cn);
            
            int totalPropuestas = statsPropuestas.get("total");
            int aprobadas = statsPropuestas.get("aprobadas");
            int rechazadas = statsPropuestas.get("rechazadas");
            
            double porcentajeAprobacion = totalPropuestas > 0 ? 
                (aprobadas * 100.0 / totalPropuestas) : 0;
            double porcentajeRechazo = totalPropuestas > 0 ? 
                (rechazadas * 100.0 / totalPropuestas) : 0;
            
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════\n");
            sb.append("        ESTADÍSTICAS GENERALES DEL SISTEMA\n");
            sb.append("═══════════════════════════════════════════════\n\n");
            
            sb.append("📊 USUARIOS:\n");
            sb.append(String.format("   Total usuarios: %d\n", statsUsuarios.get("total")));
            sb.append(String.format("   - Escritores: %d\n", statsUsuarios.get("escritores")));
            sb.append(String.format("   - Editores: %d\n", statsUsuarios.get("editores")));
            sb.append(String.format("   - Administradores: %d\n\n", statsUsuarios.get("admins")));
            
            sb.append("📝 PROPUESTAS:\n");
            sb.append(String.format("   Total propuestas: %d\n", totalPropuestas));
            sb.append(String.format("   - Enviadas: %d\n", statsPropuestas.get("enviadas")));
            sb.append(String.format("   - En revisión: %d\n", statsPropuestas.get("en_revision")));
            sb.append(String.format("   - Aprobadas: %d (%.1f%%)\n", aprobadas, porcentajeAprobacion));
            sb.append(String.format("   - Rechazadas: %d (%.1f%%)\n\n", rechazadas, porcentajeRechazo));
            
            sb.append("📚 TÍTULOS:\n");
            sb.append(String.format("   Total títulos: %d\n", statsTitulos.get("total")));
            sb.append(String.format("   - En preparación: %d\n", statsTitulos.get("en_preparacion")));
            sb.append(String.format("   - En promoción: %d\n", statsTitulos.get("en_promocion")));
            sb.append(String.format("   - Disponibles: %d\n", statsTitulos.get("disponible")));
            sb.append(String.format("   - Agotados: %d\n", statsTitulos.get("agotado")));
            
            sb.append("\n═══════════════════════════════════════════════");
            
            return sb.toString();
            
        } catch (Exception e) {
            return "Error al obtener estadísticas: " + e.getMessage();
        }
    }

    public String listarTodasPropuestas(String filtroEstado) {
        ControllerReporte ctrl = new ControllerReporte();
        
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            
            List<String> filas = ctrl.listarTodasPropuestas(cn, filtroEstado);
            
            if (filas.isEmpty()) {
                return "No hay propuestas registradas" + 
                    (filtroEstado != null && !filtroEstado.equals("TODAS") ? 
                        " con estado " + filtroEstado : "");
            }
            
            StringBuilder sb = new StringBuilder();
            String titulo = filtroEstado != null && !filtroEstado.equals("TODAS") ? 
                "PROPUESTAS - Estado: " + filtroEstado : 
                "TODAS LAS PROPUESTAS";
            
            sb.append(titulo).append("\n");
            sb.append("=".repeat(titulo.length())).append("\n\n");
            sb.append(String.join("\n", filas));
            sb.append("\n\nTotal: ").append(filas.size()).append(" propuestas");
            
            return sb.toString();
            
        } catch (Exception e) {
            return "Error al listar propuestas: " + e.getMessage();
        }
    }

    public String obtenerTopEscritores() {
        ControllerReporte ctrl = new ControllerReporte();
        
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            
            List<String> escritores = ctrl.obtenerTopEscritores(cn, 10);
            
            if (escritores.isEmpty()) {
                return "No hay escritores con propuestas registradas";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════\n");
            sb.append("          TOP 10 ESCRITORES MÁS ACTIVOS\n");
            sb.append("═══════════════════════════════════════════════\n\n");
            sb.append(String.join("\n", escritores));
            sb.append("\n\n═══════════════════════════════════════════════");
            
            return sb.toString();
            
        } catch (Exception e) {
            return "Error al obtener top escritores: " + e.getMessage();
        }
    }

    public String obtenerEstadisticasTitulos() {
        ControllerReporte ctrl = new ControllerReporte();
        
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return "No hay conexión a la base de datos";
            
            Map<String, Integer> stats = ctrl.obtenerEstadisticasTitulos(cn);
            
            int total = stats.get("total");
            
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════\n");
            sb.append("         ESTADÍSTICAS DE TÍTULOS\n");
            sb.append("═══════════════════════════════════════════════\n\n");
            
            sb.append(String.format("Total títulos creados: %d\n\n", total));
            
            if (total > 0) {
                sb.append("Distribución por estado:\n");
                sb.append(String.format("  📋 En preparación: %d (%.1f%%)\n", 
                    stats.get("en_preparacion"),
                    stats.get("en_preparacion") * 100.0 / total));
                sb.append(String.format("  📢 En promoción: %d (%.1f%%)\n", 
                    stats.get("en_promocion"),
                    stats.get("en_promocion") * 100.0 / total));
                sb.append(String.format("  ✅ Disponibles: %d (%.1f%%)\n", 
                    stats.get("disponible"),
                    stats.get("disponible") * 100.0 / total));
                sb.append(String.format("  ❌ Agotados: %d (%.1f%%)\n", 
                    stats.get("agotado"),
                    stats.get("agotado") * 100.0 / total));
            }
            
            sb.append("\n═══════════════════════════════════════════════");
            
            return sb.toString();
            
        } catch (Exception e) {
            return "Error al obtener estadísticas de títulos: " + e.getMessage();
        }
    }

    public List<ControllerReporte.PropuestaReporte> obtenerTodasPropuestasObjetos(String filtroEstado) {
        ControllerReporte ctrl = new ControllerReporte();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return new java.util.ArrayList<>();
            return ctrl.obtenerTodasPropuestasObjetos(cn, filtroEstado);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    public List<ControllerReporte.EscritorReporte> obtenerTopEscritoresObjetos() {
        ControllerReporte ctrl = new ControllerReporte();
        try (Connection cn = Conexion.getInstance().getConnection()) {
            if (cn == null) return new java.util.ArrayList<>();
            return ctrl.obtenerTopEscritoresObjetos(cn, 10);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }
}

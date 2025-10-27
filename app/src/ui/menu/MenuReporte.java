package ui.menu;

import bll.services.ReporteService;
import javax.swing.JOptionPane;

public class MenuReporte {
    
    private ReporteService reporteService;
    
    public MenuReporte() {
        this.reporteService = new ReporteService();
    }
    
    public void mostrarMenu() {
        String[] opciones = {
            "Estadísticas generales",
            "Ver todas las propuestas",
            "Propuestas por estado",
            "Estadísticas de títulos",
            "Top escritores",
            "Volver"
        };
        
        boolean salir = false;
        while (!salir) {
            int opcion = JOptionPane.showOptionDialog(
                null,
                "Seleccione un reporte:",
                "Reportes y Estadísticas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            switch (opcion) {
                case 0:
                    verEstadisticasGenerales();
                    break;
                case 1:
                    verTodasPropuestas();
                    break;
                case 2:
                    verPropuestasPorEstado();
                    break;
                case 3:
                    verEstadisticasTitulos();
                    break;
                case 4:
                    verTopEscritores();
                    break;
                case 5:
                case JOptionPane.CLOSED_OPTION:
                    salir = true;
                    break;
            }
        }
    }
    
    private void verEstadisticasGenerales() {
        String resultado = reporteService.obtenerEstadisticasGenerales();
        JOptionPane.showMessageDialog(
            null,
            resultado,
            "Estadísticas Generales",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void verTodasPropuestas() {
        String resultado = reporteService.listarTodasPropuestas("TODAS");
        JOptionPane.showMessageDialog(
            null,
            resultado,
            "Todas las Propuestas",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void verPropuestasPorEstado() {
        String[] estados = {
            "ENVIADA",
            "EN_REVISION",
            "APROBADA",
            "RECHAZADA"
        };
        
        String estado = (String) JOptionPane.showInputDialog(
            null,
            "Seleccione el estado:",
            "Filtrar por Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estados,
            estados[0]
        );
        
        if (estado != null) {
            String resultado = reporteService.listarTodasPropuestas(estado);
            JOptionPane.showMessageDialog(
                null,
                resultado,
                "Propuestas - Estado: " + estado,
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    private void verEstadisticasTitulos() {
        String resultado = reporteService.obtenerEstadisticasTitulos();
        JOptionPane.showMessageDialog(
            null,
            resultado,
            "Estadísticas de Títulos",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void verTopEscritores() {
        String resultado = reporteService.obtenerTopEscritores();
        JOptionPane.showMessageDialog(
            null,
            resultado,
            "Top Escritores",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}

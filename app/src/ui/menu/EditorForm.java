package ui.menu;

import bll.services.PropuestaService;
import bll.services.ReporteService;
import bll.services.TituloService;
import bll.usuarios.Editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorForm extends JFrame{
    private JPanel rootPanel;
    private JPanel panelMenu;
    private JButton PropuestasButton;
    private JButton TitulosButton;
    private JButton ReportesButton;
    private JButton SalirButton;

    private JButton revisarButton;
    private JButton volverButton;
    private JPanel panelPropuestas;
    private JTabbedPane tabPropuestas;
    private JPanel panelMisPropuestas;
    private JPanel panelBandeja;
    private JTable tblMisPropuestas;
    private JTable tblBandejaPropuestas;
    private JButton crearTítuloButton;
    private JButton definirCondicionesButton;
    private JButton asignarEditorButton;
    private JPanel panelContenido;
    private JPanel panelTitulos;
    private JPanel panelReportes;
    private JTable tblTitulos;
    private JButton btnTransferirAMarketing;
    private JButton btnActualizarEstadoComercializacion;
    private JPanel pnlActions;
    private JButton btnVolver;
    private JTabbedPane TabReportes;
    private JPanel tabEstadisticasGenerales;
    private JPanel TabTodasPropuestas;
    private JPanel tabEstadisticasTitulos;
    private JPanel tabTopEscritores;
    private JPanel panelFiltroReportePropuestas;
    private JLabel lblEstadoFiltro;
    private JComboBox cbEstadoReporte;
    private JButton btnFiltrar;
    private JTable tblReportePropuestas;
    private JTable tblTopEscritores;
    private JTextArea txtEstadisticasTitulos;
    private JTextArea txtEstTitulos;
    private JButton volverButton1;

    private final PropuestaService propuestaService = new PropuestaService();
    private final TituloService tituloService = new TituloService();
    private final ReporteService reporteService = new ReporteService();
    private final Editor editorActual;

    public EditorForm(Editor editor) {
            this.editorActual = editor;
            setContentPane(rootPanel);
            setTitle("Editor - Menú");
            setSize(800, 600);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            rootPanel.setLayout(cardLayout);
            rootPanel.removeAll();
            rootPanel.add(panelMenu, "menu");
            rootPanel.add(panelPropuestas, "propuestas");
            rootPanel.add(panelTitulos, "titulos");
            rootPanel.add(panelReportes, "reportes");

            cardLayout.show(rootPanel, "menu");


        panelMenu.setLayout(new java.awt.GridLayout(4, 1, 0, 20));
            panelMenu.setBorder(BorderFactory.createEmptyBorder(60, 200, 60, 200));
            java.awt.Color blue = new java.awt.Color(224, 255, 255);
            java.awt.Color purple = new java.awt.Color(245, 231, 255);
            java.awt.Color yellow = new java.awt.Color(255, 253, 240);
            java.awt.Color pink = new java.awt.Color(255, 224, 224);


            PropuestasButton.setBackground(blue);
            TitulosButton.setBackground(purple);
            ReportesButton.setBackground(yellow);
            SalirButton.setBackground(pink);

            inicializarTablasEditor();
            inicializarTablaTitulos();
            inicializarTablasReportes();
            cargarBandeja();
            cargarMisPropuestas();
            cargarTitulos();

            PropuestasButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) rootPanel.getLayout();
                    cl.show(rootPanel, "propuestas");
                }
            });

            TitulosButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cargarTitulos();
                    CardLayout cl = (CardLayout) rootPanel.getLayout();
                    cl.show(rootPanel, "titulos");
                }
            });

            ReportesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cargarReportes();
                    CardLayout cl = (CardLayout) rootPanel.getLayout();
                    cl.show(rootPanel, "reportes");
                }
            });

            SalirButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new StartForm();
                }
            });

        revisarButton.addActionListener(e -> revisarSeleccionada());

        volverButton.addActionListener(e -> showMenu());

        if (tabPropuestas != null) {
            tabPropuestas.setSelectedIndex(0);
        }

        setVisible(true);
        asignarEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarEditorASeleccionada();
            }
        });
        crearTítuloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearTituloDesdeSeleccionada();
            }
        });
        definirCondicionesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                definirCondicionesParaSeleccionada();
            }
        });
        btnTransferirAMarketing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferirAMarketing();
            }
        });
        btnActualizarEstadoComercializacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEstadoComercializacion();
            }
        });
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMenu();
            }
        });
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPropuestas();
            }
        });
        volverButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMenu();
            }
        });
    }

    private void inicializarTablasEditor() {

        DefaultTableModel bandejaModel = new DefaultTableModel(
                new Object[]{"ID", "Autor", "Título", "Resumen", "Estado", "Fecha", "Archivo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBandejaPropuestas.setModel(bandejaModel);
        tblBandejaPropuestas.setRowHeight(26);
        tblBandejaPropuestas.getColumnModel().getColumn(0).setMinWidth(0);
        tblBandejaPropuestas.getColumnModel().getColumn(0).setMaxWidth(0);
        tblBandejaPropuestas.getColumnModel().getColumn(0).setWidth(0);

        DefaultTableModel misModel = new DefaultTableModel(
                new Object[]{"ID", "Autor", "Título", "Resumen", "Estado", "Fecha", "Archivo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMisPropuestas.setModel(misModel);
        tblMisPropuestas.setRowHeight(26);
        tblMisPropuestas.getColumnModel().getColumn(0).setMinWidth(0);
        tblMisPropuestas.getColumnModel().getColumn(0).setMaxWidth(0);
        tblMisPropuestas.getColumnModel().getColumn(0).setWidth(0);
    }

    private void cargarBandeja() {
        DefaultTableModel model = (DefaultTableModel) tblBandejaPropuestas.getModel();
        model.setRowCount(0);

        var propuestas = propuestaService.obtenerPorEditor();
        for (var p : propuestas) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getAutor(),
                    p.getTitulo(),
                    p.getResumen(),
                    p.getEstado(),
                    p.getFechaCreacion(),
                    p.getArchivoUrl()
            });
        }
    }

    private void cargarMisPropuestas() {
        DefaultTableModel model = (DefaultTableModel) tblMisPropuestas.getModel();
        model.setRowCount(0);

        if (editorActual == null) {
            return;
        }

        var propuestas = propuestaService.obtenerPorEditor(editorActual.getId());
        for (var p : propuestas) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getAutor(),
                    p.getTitulo(),
                    p.getResumen(),
                    p.getEstado(),
                    p.getFechaCreacion(),
                    p.getArchivoUrl()
            });
        }
    }



    private void showMenu() {
        CardLayout cl = (CardLayout) rootPanel.getLayout();
        cl.show(rootPanel, "menu");
    }

    private void showPropuestas() {
        CardLayout cl = (CardLayout) rootPanel.getLayout();
        cl.show(rootPanel, "propuestas");
    }

    private void revisarSeleccionada() {
        if (tabPropuestas == null) return;

        int idx = tabPropuestas.getSelectedIndex();
        JTable tablaActual = (idx == 0) ? tblMisPropuestas : tblBandejaPropuestas;
        if (tablaActual == null) return;

        int fila = tablaActual.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná primero una propuesta de la lista.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int propuestaId = (int) tablaActual.getValueAt(fila, 0);
        String autor   = String.valueOf(tablaActual.getValueAt(fila, 1));
        String titulo  = String.valueOf(tablaActual.getValueAt(fila, 2));
        String resumen = String.valueOf(tablaActual.getValueAt(fila, 3));
        String estado  = String.valueOf(tablaActual.getValueAt(fila, 4));
        String fecha   = String.valueOf(tablaActual.getValueAt(fila, 5));
        String archivo = String.valueOf(tablaActual.getValueAt(fila, 6));

        RevisarPropuestaDialog dlg = new RevisarPropuestaDialog(
                this,
                propuestaId,
                Integer.parseInt(editorActual.getId()),
                autor,
                titulo,
                resumen,
                estado,
                fecha,
                archivo,
                () -> {
                    cargarBandeja();
                    cargarMisPropuestas();
                }
        );
        dlg.setVisible(true);
    }

    private void asignarEditorASeleccionada() {
        if (tabPropuestas == null) return;

        int idx = tabPropuestas.getSelectedIndex();
        // Solo se puede asignar desde la bandeja (tab 1)
        if (idx != 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo se pueden asignar propuestas desde la Bandeja.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JTable tablaActual = tblBandejaPropuestas;
        int fila = tablaActual.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná primero una propuesta de la lista.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int propuestaId = (int) tablaActual.getValueAt(fila, 0);
        String titulo = String.valueOf(tablaActual.getValueAt(fila, 2));

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Querés asignarte la propuesta \"" + titulo + "\"?",
                "Confirmar asignación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            String resultado = propuestaService.asignarAEditor(
                    String.valueOf(propuestaId),
                    Integer.parseInt(editorActual.getId())
            );
            
            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Asignar Editor",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarBandeja();
            cargarMisPropuestas();
        }
    }

    private void crearTituloDesdeSeleccionada() {
        if (tabPropuestas == null) return;

        int idx = tabPropuestas.getSelectedIndex();
        JTable tablaActual = (idx == 0) ? tblMisPropuestas : tblBandejaPropuestas;
        if (tablaActual == null) return;

        int fila = tablaActual.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná primero una propuesta de la lista.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int propuestaId = (int) tablaActual.getValueAt(fila, 0);
        String tituloExistente = String.valueOf(tablaActual.getValueAt(fila, 2));

        // Pedir el título del libro
        String tituloLibro = JOptionPane.showInputDialog(
                this,
                "Ingresá el título del libro:",
                tituloExistente
        );

        if (tituloLibro != null && !tituloLibro.trim().isEmpty()) {
            String resultado = tituloService.crearTitulo(
                    String.valueOf(propuestaId),
                    tituloLibro.trim()
            );

            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Crear Título",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarBandeja();
            cargarMisPropuestas();
        }
    }

    private void definirCondicionesParaSeleccionada() {
        if (tabPropuestas == null) return;

        int idx = tabPropuestas.getSelectedIndex();
        JTable tablaActual = (idx == 0) ? tblMisPropuestas : tblBandejaPropuestas;
        if (tablaActual == null) return;

        int fila = tablaActual.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná primero una propuesta de la lista.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int propuestaId = (int) tablaActual.getValueAt(fila, 0);
        String titulo = String.valueOf(tablaActual.getValueAt(fila, 2));

        // Pedir tirada inicial
        String tiradaStr = JOptionPane.showInputDialog(
                this,
                "Tirada inicial para \"" + titulo + "\":",
                "Definir Condiciones - Tirada",
                JOptionPane.QUESTION_MESSAGE
        );

        if (tiradaStr == null || tiradaStr.trim().isEmpty()) {
            return; // Usuario canceló
        }

        // Pedir porcentaje de ganancias
        String porcentajeStr = JOptionPane.showInputDialog(
                this,
                "Porcentaje de ganancias para el autor (0-100):",
                "Definir Condiciones - Porcentaje",
                JOptionPane.QUESTION_MESSAGE
        );

        if (porcentajeStr == null || porcentajeStr.trim().isEmpty()) {
            return; // Usuario canceló
        }

        // Pedir observaciones (opcional)
        String observaciones = JOptionPane.showInputDialog(
                this,
                "Observaciones (opcional):",
                "Definir Condiciones - Observaciones",
                JOptionPane.QUESTION_MESSAGE
        );

        String resultado = tituloService.definirCondiciones(
                String.valueOf(propuestaId),
                tiradaStr.trim(),
                porcentajeStr.trim(),
                observaciones
        );

        JOptionPane.showMessageDialog(
                this,
                resultado,
                "Definir Condiciones",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarBandeja();
        cargarMisPropuestas();
    }

    private void inicializarTablaTitulos() {
        DefaultTableModel titulosModel = new DefaultTableModel(
                new Object[]{"ID", "Título", "Estado Comercialización", "Propuesta ID"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTitulos.setModel(titulosModel);
        tblTitulos.setRowHeight(26);
        tblTitulos.getColumnModel().getColumn(0).setMinWidth(0);
        tblTitulos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblTitulos.getColumnModel().getColumn(0).setWidth(0);
        tblTitulos.getColumnModel().getColumn(3).setMinWidth(0);
        tblTitulos.getColumnModel().getColumn(3).setMaxWidth(0);
        tblTitulos.getColumnModel().getColumn(3).setWidth(0);
    }

    private void cargarTitulos() {
        DefaultTableModel model = (DefaultTableModel) tblTitulos.getModel();
        model.setRowCount(0);

        var titulos = tituloService.obtenerTodosTitulos();
        for (var t : titulos) {
            model.addRow(new Object[]{
                    t.getId(),
                    t.getTitulo(),
                    t.getEstadoComercializacion().name().replace("_", " "),
                    t.getPropuestaId()
            });
        }
    }

    private void transferirAMarketing() {
        int fila = tblTitulos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná primero un título de la lista.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int tituloId = (int) tblTitulos.getValueAt(fila, 0);
        String titulo = String.valueOf(tblTitulos.getValueAt(fila, 1));

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Querés transferir el título \"" + titulo + "\" a Marketing/Ventas?",
                "Confirmar transferencia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            String resultado = tituloService.transferirAMarketing(String.valueOf(tituloId));

            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Transferir a Marketing",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTitulos();
        }
    }

    private void actualizarEstadoComercializacion() {
        int fila = tblTitulos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná primero un título de la lista.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int tituloId = (int) tblTitulos.getValueAt(fila, 0);
        String titulo = String.valueOf(tblTitulos.getValueAt(fila, 1));

        String[] opciones = {"EN_PREPARACION", "EN_PROMOCION", "DISPONIBLE", "AGOTADO"};
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccioná el nuevo estado para \"" + titulo + "\":",
                "Actualizar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null) {
            String resultado = tituloService.actualizarEstadoComercializacion(
                    String.valueOf(tituloId),
                    seleccion
            );

            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Actualizar Estado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTitulos();
        }
    }

    private void inicializarTablasReportes() {
        // Tabla de todas las propuestas
        DefaultTableModel propuestasReporteModel = new DefaultTableModel(
                new Object[]{"ID", "Título", "Escritor", "Estado", "Fecha"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblReportePropuestas.setModel(propuestasReporteModel);
        tblReportePropuestas.setRowHeight(26);
        tblReportePropuestas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblReportePropuestas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblReportePropuestas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblReportePropuestas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblReportePropuestas.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Tabla de top escritores
        DefaultTableModel topEscritoresModel = new DefaultTableModel(
                new Object[]{"Posición", "Escritor", "Total Propuestas", "Aprobadas", "Tasa Aprobación"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTopEscritores.setModel(topEscritoresModel);
        tblTopEscritores.setRowHeight(26);
        tblTopEscritores.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblTopEscritores.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblTopEscritores.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblTopEscritores.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblTopEscritores.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Inicializar ComboBox de estados
        cbEstadoReporte.removeAllItems();
        cbEstadoReporte.addItem("TODAS");
        cbEstadoReporte.addItem("BORRADOR");
        cbEstadoReporte.addItem("ENVIADA");
        cbEstadoReporte.addItem("EN_REVISION");
        cbEstadoReporte.addItem("APROBADA");
        cbEstadoReporte.addItem("RECHAZADA");
    }

    private void cargarReportes() {
        // Cargar estadísticas generales - buscar JTextArea recursivamente
        String estadisticasGenerales = reporteService.obtenerEstadisticasGenerales();
        JTextArea txtEstadisticasGenerales = buscarTextArea(tabEstadisticasGenerales);
        if (txtEstadisticasGenerales != null) {
            txtEstadisticasGenerales.setText(estadisticasGenerales);
        }

        // Cargar estadísticas de títulos
        String estadisticasTitulos = reporteService.obtenerEstadisticasTitulos();
        JTextArea txtEstTitulos = buscarTextArea(tabEstadisticasTitulos);
        if (txtEstTitulos != null) {
            txtEstTitulos.setText(estadisticasTitulos);
        }

        // Cargar todas las propuestas
        cargarTablaPropuestasReporte("TODAS");

        // Cargar top escritores
        cargarTablaTopEscritores();
    }

    private JTextArea buscarTextArea(java.awt.Container container) {
        if (container == null) return null;
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextArea textArea) {
                return textArea;
            } else if (comp instanceof java.awt.Container childContainer) {
                JTextArea result = buscarTextArea(childContainer);
                if (result != null) return result;
            }
        }
        return null;
    }

    private void cargarTablaPropuestasReporte(String filtroEstado) {
        DefaultTableModel model = (DefaultTableModel) tblReportePropuestas.getModel();
        model.setRowCount(0);

        var propuestas = reporteService.obtenerTodasPropuestasObjetos(filtroEstado);
        for (var p : propuestas) {
            model.addRow(new Object[]{
                    p.id,
                    p.titulo,
                    p.escritor,
                    p.estado,
                    p.fechaCreacion
            });
        }
    }

    private void cargarTablaTopEscritores() {
        DefaultTableModel model = (DefaultTableModel) tblTopEscritores.getModel();
        model.setRowCount(0);

        var escritores = reporteService.obtenerTopEscritoresObjetos();
        int posicion = 1;
        for (var e : escritores) {
            double tasa = e.totalPropuestas > 0 ? (e.aprobadas * 100.0 / e.totalPropuestas) : 0;
            model.addRow(new Object[]{
                    posicion++,
                    e.nombre,
                    e.totalPropuestas,
                    e.aprobadas,
                    String.format("%.1f%%", tasa)
            });
        }
    }

    private void filtrarPropuestas() {
        String estadoSeleccionado = (String) cbEstadoReporte.getSelectedItem();
        if (estadoSeleccionado != null) {
            cargarTablaPropuestasReporte(estadoSeleccionado);
        }
    }

}


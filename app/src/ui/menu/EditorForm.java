package ui.menu;

import bll.services.PropuestaService;

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

    private final PropuestaService propuestaService = new PropuestaService();

    public EditorForm() {
            setContentPane(rootPanel);
            setTitle("Editor - Menú");
            setSize(800, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            rootPanel.removeAll();
            rootPanel.setLayout(new CardLayout());
            rootPanel.add(panelMenu, "menu");
            rootPanel.add(panelPropuestas, "propuestas");
            showMenu();


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
            cargarBandeja();

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
                    // TODO: заменить на реальную форму títulos
                    JOptionPane.showMessageDialog(
                            EditorForm.this,
                            "Aquí se abrirá la sección de TÍTULOS.",
                            "Títulos",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            });

            ReportesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO: заменить на реальный экран reportes
                    JOptionPane.showMessageDialog(
                            EditorForm.this,
                            "Aquí se abrirá la sección de REPORTES.",
                            "Reportes",
                            JOptionPane.INFORMATION_MESSAGE
                    );
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
    }

    private void inicializarTablasEditor() {

        DefaultTableModel bandejaModel = new DefaultTableModel(
                new Object[]{"Autor", "Título", "Resumen", "Estado", "Fecha", "Archivo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBandejaPropuestas.setModel(bandejaModel);
        tblBandejaPropuestas.setRowHeight(26);

        DefaultTableModel misModel = new DefaultTableModel(
                new Object[]{"Autor", "Título", "Resumen", "Estado", "Fecha", "Archivo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMisPropuestas.setModel(misModel);
        tblMisPropuestas.setRowHeight(26);
    }

    private void cargarBandeja() {
        DefaultTableModel model = (DefaultTableModel) tblBandejaPropuestas.getModel();
        model.setRowCount(0);

        var propuestas = propuestaService.obtenerPorEditor();
        for (var p : propuestas) {
            model.addRow(new Object[]{
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

        String autor   = String.valueOf(tablaActual.getValueAt(fila, 0));
        String titulo  = String.valueOf(tablaActual.getValueAt(fila, 1));
        String resumen = String.valueOf(tablaActual.getValueAt(fila, 2));
        String estado  = String.valueOf(tablaActual.getValueAt(fila, 3));
        String fecha   = String.valueOf(tablaActual.getValueAt(fila, 4));
        String archivo = String.valueOf(tablaActual.getValueAt(fila, 5));

        RevisarPropuestaDialog dlg = new RevisarPropuestaDialog(
                this,
                autor,
                titulo,
                resumen,
                estado,
                fecha,
                archivo
        );
        dlg.setVisible(true);
    }

}


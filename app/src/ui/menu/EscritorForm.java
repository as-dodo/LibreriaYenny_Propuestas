package ui.menu;

import bll.usuarios.Escritor;
import bll.propuestas.Propuesta;
import bll.services.PropuestaService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EscritorForm extends JFrame {
    private JPanel rootPanel;
    private JTable tblMisPropuestas;
    private JButton btnNueva;
    private JPanel panelLista;
    private JPanel panelCrear;
    private JTextField tfTitulo;
    private JTextField tfResumen;
    private JTextField tfLink;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton salirButton;
    private JLabel lblImagen;
    private final Escritor escritor;
    private final PropuestaService propuestaService;

    public EscritorForm(Escritor escritor) {
        this.escritor = escritor;
        this.propuestaService = new PropuestaService();

        setContentPane(rootPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/icon_book_flower.png"));
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        lblImagen.setIcon(new ImageIcon(scaled));

        rootPanel.setLayout(new CardLayout());
        rootPanel.add(panelLista, "lista");
        rootPanel.add(panelCrear, "crear");
        setTitle("Mis Propuestas - Escritor");
        setSize(700, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        inicializarTabla();
        cargarPropuestas();


        btnNueva.addActionListener(e -> {
            CardLayout cl = (CardLayout) rootPanel.getLayout();
            cl.show(rootPanel, "crear");
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StartForm();
            }
        });


        btnGuardar.addActionListener(e -> {
            String titulo = tfTitulo.getText().trim();
            String resumen = tfResumen.getText().trim();
            String link = tfLink.getText().trim();

            if (titulo.isEmpty() || resumen.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Título y Resumen son obligatorios.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String resultado = propuestaService.enviarPropuesta(
                    String.valueOf(escritor.getId()),
                    titulo,
                    resumen,
                    link.isBlank() ? null : link
            );

            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.equals("Propuesta enviada")) {
                cargarPropuestas();

                tfTitulo.setText("");
                tfResumen.setText("");
                tfLink.setText("");


                CardLayout cl = (CardLayout) rootPanel.getLayout();
                cl.show(rootPanel, "lista");
            }
        });

        btnCancelar.addActionListener(e -> {
            tfTitulo.setText("");
            tfResumen.setText("");
            tfLink.setText("");

            CardLayout cl = (CardLayout) rootPanel.getLayout();
            cl.show(rootPanel, "lista");
        });

    }

    private void inicializarTabla() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Título", "Resumen", "Fecha", "Estado"}, 0
        );
        tblMisPropuestas.setModel(model);


        tblMisPropuestas.setFillsViewportHeight(true);
        tblMisPropuestas.setRowHeight(28);

        Color seaRow = new Color(235, 249, 247);
        Color seaSelected = new Color(0, 150, 136);
        Color seaHeader = new Color(224, 242, 241);


        tblMisPropuestas.setSelectionBackground(seaSelected);
        tblMisPropuestas.setSelectionForeground(Color.WHITE);


        tblMisPropuestas.setShowHorizontalLines(true);
        tblMisPropuestas.setShowVerticalLines(false);
        tblMisPropuestas.setGridColor(new Color(200, 220, 220));


        JTableHeader header = tblMisPropuestas.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(seaHeader);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setOpaque(true);


        tblMisPropuestas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(seaSelected);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(seaRow);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        tblMisPropuestas.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblMisPropuestas.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblMisPropuestas.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblMisPropuestas.getColumnModel().getColumn(3).setPreferredWidth(100);
    }


    private void cargarPropuestas() {
        DefaultTableModel model = (DefaultTableModel) tblMisPropuestas.getModel();
        model.setRowCount(0);

        List<Propuesta> propuestas = propuestaService.obtenerPorEscritor(escritor.getId());
        for (Propuesta p : propuestas) {
            model.addRow(new Object[]{
                    p.getTituloPropuesto(),
                    p.getResumen(),
                    p.getFechaCreacion(),
                    p.getEstado(),
                    p.getFechaDecision()
            });
        }
    }
}

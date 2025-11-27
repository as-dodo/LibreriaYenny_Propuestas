package ui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RevisarPropuestaDialog extends JDialog {
    private JPanel rootPanel;
    private JLabel lblAutor;
    private JLabel lblTitulo;
    private JTextArea taResumen;
    private JLabel lblArchivo;
    private JLabel lblEstado;
    private JLabel lblFecha;
    private JLabel Autor;
    private JLabel Archivo;
    private JLabel Resumen;
    private JLabel Estado;
    private JLabel Fecha;
    private JLabel Titulo;
    private JButton btnComentar;
    private JButton btnRechazar;
    private JButton btnAprobar;
    private JButton btnCerrar;

    public RevisarPropuestaDialog(Frame owner,
                                  String autor,
                                  String titulo,
                                  String resumen,
                                  String estado,
                                  String fecha,
                                  String archivoUrl) {

        super(owner, "Revisar propuesta", true);

        setContentPane(rootPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        lblAutor.setText(autor);
        lblTitulo.setText(titulo);
        lblEstado.setText(estado);
        lblFecha.setText(fecha);

        taResumen.setText(resumen != null ? resumen : "");
        taResumen.setEditable(false);
        taResumen.setLineWrap(true);
        taResumen.setWrapStyleWord(true);

        lblArchivo.setText(
                (archivoUrl != null && !archivoUrl.isBlank())
                        ? archivoUrl
                        : "-"
        );

        btnCerrar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
        btnAprobar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnRechazar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnComentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}

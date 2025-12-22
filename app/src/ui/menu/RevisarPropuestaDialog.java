package ui.menu;

import bll.services.PropuestaService;
import repository.FileStorage;

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
    private JButton btnDescargar;

    private final int propuestaId;
    private final int editorId;
    private final PropuestaService propuestaService = new PropuestaService();
    private final Runnable onActionCallback;
    private final String archivoUrl;

    public RevisarPropuestaDialog(Frame owner,
                                  int propuestaId,
                                  int editorId,
                                  String autor,
                                  String titulo,
                                  String resumen,
                                  String estado,
                                  String fecha,
                                  String archivoUrl,
                                  Runnable onActionCallback) {

        super(owner, "Revisar propuesta", true);

        this.propuestaId = propuestaId;
        this.editorId = editorId;
        this.onActionCallback = onActionCallback;
        this.archivoUrl = archivoUrl;

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
                        ? FileStorage.getFileName(archivoUrl)
                        : "-"
        );

        btnCerrar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
        btnAprobar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resultado = propuestaService.aprobar(String.valueOf(propuestaId), editorId);
                JOptionPane.showMessageDialog(
                        RevisarPropuestaDialog.this,
                        resultado,
                        "Aprobar Propuesta",
                        JOptionPane.INFORMATION_MESSAGE
                );
                if (onActionCallback != null) {
                    onActionCallback.run();
                }
                dispose();
            }
        });
        btnRechazar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resultado = propuestaService.rechazar(String.valueOf(propuestaId), editorId);
                JOptionPane.showMessageDialog(
                        RevisarPropuestaDialog.this,
                        resultado,
                        "Rechazar Propuesta",
                        JOptionPane.INFORMATION_MESSAGE
                );
                if (onActionCallback != null) {
                    onActionCallback.run();
                }
                dispose();
            }
        });
        btnComentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comentario = JOptionPane.showInputDialog(
                        RevisarPropuestaDialog.this,
                        "Ingresa tu comentario:",
                        "Agregar Comentario",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (comentario != null && !comentario.trim().isEmpty()) {
                    String resultado = propuestaService.agregarComentario(
                            String.valueOf(propuestaId),
                            editorId,
                            comentario.trim()
                    );
                    JOptionPane.showMessageDialog(
                            RevisarPropuestaDialog.this,
                            resultado,
                            "Comentario",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        btnDescargar.addActionListener(e -> descargarArchivo());
    }

    private void descargarArchivo() {
        if (archivoUrl == null || archivoUrl.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La propuesta no tiene archivo adjunto.",
                    "Descargar",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(FileStorage.getFileName(archivoUrl)));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        try {
            FileStorage.copyTo(archivoUrl, chooser.getSelectedFile().toPath());
            JOptionPane.showMessageDialog(
                    this,
                    "Archivo descargado correctamente.",
                    "Descargar",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo descargar el archivo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

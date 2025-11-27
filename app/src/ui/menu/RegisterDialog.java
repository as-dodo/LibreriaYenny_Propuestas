package ui.menu;

import bll.services.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterDialog extends JDialog {
    private JPanel rootPanel;
    private JLabel lbNombre;
    private JTextField tfNombre;
    private JLabel emailLabel;
    private JTextField tfEmail;
    private JPasswordField passwordField;
    private JButton guardarButton;
    private JButton cancelarButton;

    private boolean registrado = false;


    public RegisterDialog(Frame owner) {
        super(owner, "Registrarse", true);
        setContentPane(rootPanel);
        getRootPane().setDefaultButton(guardarButton);


        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = tfNombre.getText().trim();
                String email = tfEmail.getText().trim();
                String pass = new String(passwordField.getPassword()).trim();

                if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(rootPanel,
                            "Por favor, complete todos los campos.",
                            "Campos vacíos",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                AuthService auth = new AuthService();
                String resultado = auth.registrarse(nombre, email, pass);

                JOptionPane.showMessageDialog(rootPanel,
                        resultado,
                        "Resultado del registro",
                        JOptionPane.INFORMATION_MESSAGE);

                registrado = true;
                dispose();

            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrado = false;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterDialog dlg = new RegisterDialog(null);
            dlg.setVisible(true);
            System.out.println("Registrado: " + dlg.isRegistrado());
        });
    }
}

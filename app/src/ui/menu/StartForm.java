package ui.menu;

import bll.services.AuthService;
import bll.usuarios.Escritor;
import bll.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartForm extends JFrame {
    private JPanel rootPanel;
    private JLabel lblWelcome;
    private JButton LoginButton;
    private JButton registrarseButton;
    private JLabel lblLogo;
    private JButton salirButton;


    public StartForm() {
        setContentPane(rootPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.jpeg"));
        Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaled));

        setTitle("Yenny - Inicio");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        AuthService auth = new AuthService();

        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDialog dlg = new LoginDialog(StartForm.this);
                dlg.setVisible(true);

                String email = dlg.getEmail();
                String pass = dlg.getPassword();

                if (email == null || pass == null) return;


                Usuario usuario = auth.autenticar(email, pass);
                if (usuario == null) {
                    JOptionPane.showMessageDialog(StartForm.this,
                            "Email o contraseña incorrectos.",
                            "Error de login",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(StartForm.this,
                        "¡Bienvenido, " + usuario.getNombre() +
                                "! Sesión iniciada como " + usuario.getRol() + ".");

                switch (usuario.getRol()) {
//                    case ESCRITOR -> new MenuEscritor((Escritor) usuario).run();
                    case ESCRITOR -> {
                        dispose();
                        new EscritorForm((Escritor) usuario);
                    }
                    case EDITOR   -> new MenuEditor(usuario).run();
                    case ADMIN    -> new MenuAdmin().run();
                    default       -> {}
                }
            }
        });

        registrarseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterDialog dlg = new RegisterDialog(StartForm.this);
                dlg.setVisible(true);

                if (dlg.isRegistrado()) {
                    JOptionPane.showMessageDialog(StartForm.this,
                            "Registro completado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartForm::new);
    }
}

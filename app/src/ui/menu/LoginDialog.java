package ui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {

    private JPanel rootPanel;
    private JTextField tfEmail;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton salirButton;
    private String emailValue;
    private String passwordValue;

    public LoginDialog(Frame owner){
        super(owner, "Iniciar sesión", true);
        setContentPane(rootPanel);
        getRootPane().setDefaultButton(loginButton);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailValue = tfEmail.getText();
                passwordValue = new String (passwordField.getPassword());

//                emailValue = email;
//                passwordValue = password;

                dispose();
            }
        });


        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public String getEmail() {
        return emailValue;
    }

    public String getPassword() {
        return passwordValue;
    }

//    public static void main(String[] args){
//        SwingUtilities.invokeLater(() -> {
//            LoginDialog dlg = new LoginDialog(null);
//            dlg.setVisible(true);
//        });

//    }
}

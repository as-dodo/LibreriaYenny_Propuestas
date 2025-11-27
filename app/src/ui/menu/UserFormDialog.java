package ui.menu;

import bll.services.AdminService;
import bll.usuarios.Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserFormDialog extends JDialog {
    private JPanel rootPanel;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JComboBox<String> cmbRol;
    private JPasswordField txtPassword;
    private JButton guardarButton;
    private JButton salirButton;
    private JLabel rol;

    private final AdminService adminService = new AdminService();
    private final Usuario usuarioEditar;
    private final Runnable onSaveCallback;
    private boolean guardadoExitoso = false;

    // Constructor para crear nuevo usuario
    public UserFormDialog(JFrame owner, Runnable onSaveCallback) {
        this(owner, null, onSaveCallback);
    }

    // Constructor para editar usuario existente
    public UserFormDialog(JFrame owner, Usuario usuario, Runnable onSaveCallback) {
        super(owner, usuario == null ? "Crear Usuario" : "Modificar Usuario", true);

        this.usuarioEditar = usuario;
        this.onSaveCallback = onSaveCallback;

        setContentPane(rootPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarUi();

        pack();
        setLocationRelativeTo(owner);
    }

    private void inicializarUi() {
        cmbRol.setModel(new DefaultComboBoxModel<>(new String[]{
                "ADMIN", "EDITOR", "ESCRITOR"
        }));

        txtNombre.setToolTipText("Nombre y apellido del usuario");
        txtEmail.setToolTipText("Correo electrónico");
        txtPassword.setToolTipText("Contraseña");

        // Si estamos editando, rellenar los campos
        if (usuarioEditar != null) {
            txtNombre.setText(usuarioEditar.getNombre());
            txtEmail.setText(usuarioEditar.getEmail());
            cmbRol.setSelectedItem(usuarioEditar.getRol().name());
            
            // La contraseña no es obligatoria al editar
            txtPassword.setToolTipText("Dejar en blanco para mantener la contraseña actual");
        }

        salirButton.addActionListener(e -> dispose());

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarUsuario();
            }
        });
    }

    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String rolSeleccionado = (String) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nombre y email son obligatorios.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String resultado;

        if (usuarioEditar == null) {
            // Crear nuevo usuario
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "La contraseña es obligatoria para crear un usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            resultado = adminService.crearUsuario(nombre, email, password, rolSeleccionado);
        } else {
            // Modificar usuario existente
            resultado = adminService.modificarUsuario(usuarioEditar.getEmail(), nombre, email);
            
            // Si también cambió el rol
            if (!usuarioEditar.getRol().name().equals(rolSeleccionado)) {
                String resultadoRol = adminService.asignarRol(email, rolSeleccionado);
                resultado = resultado + "\n" + resultadoRol;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                resultado,
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (resultado.contains("correctamente") || resultado.contains("creado")) {
            guardadoExitoso = true;
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            dispose();
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}

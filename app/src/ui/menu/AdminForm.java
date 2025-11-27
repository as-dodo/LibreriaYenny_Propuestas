package ui.menu;

import bll.services.AdminService;
import bll.usuarios.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminForm extends JFrame {
    private JPanel rootPanel;
    private JPanel panelAdmin;
    private JTable tableUsers;
    private JPanel panelAdminAction;
    private JButton crearUsuarioButton;
    private JButton eliminarUsuarioButton;
    private JButton modificarUsuarioButton;
    private JButton salirButton;

    private final AdminService adminService = new AdminService();

    public AdminForm() {
        setContentPane(rootPanel);
        setTitle("Administrador - Usuarios");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        inicializarTabla();
        inicializarEventos();
        cargarUsuarios();

        setVisible(true);
    }

    private void inicializarTabla() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Email", "Rol"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableUsers.setModel(model);
        tableUsers.setRowHeight(26);
        
        // Ocultar la columna ID
        tableUsers.getColumnModel().getColumn(0).setMinWidth(0);
        tableUsers.getColumnModel().getColumn(0).setMaxWidth(0);
        tableUsers.getColumnModel().getColumn(0).setWidth(0);
    }

    private void cargarUsuarios() {
        DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
        model.setRowCount(0);

        List<Usuario> usuarios = adminService.obtenerTodosLosUsuarios();
        for (Usuario u : usuarios) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getNombre(),
                    u.getEmail(),
                    u.getRol().name()
            });
        }
    }

    private void inicializarEventos() {

        crearUsuarioButton.addActionListener(e -> {
            UserFormDialog dialog = new UserFormDialog(this, () -> cargarUsuarios());
            dialog.setVisible(true);
        });

        modificarUsuarioButton.addActionListener(e -> {
            int fila = tableUsers.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccioná primero un usuario de la lista.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String userId = String.valueOf(tableUsers.getValueAt(fila, 0));
            String nombre = String.valueOf(tableUsers.getValueAt(fila, 1));
            String email = String.valueOf(tableUsers.getValueAt(fila, 2));
            String rol = String.valueOf(tableUsers.getValueAt(fila, 3));

            // Crear un usuario temporal para edición
            Usuario usuarioEditar = crearUsuarioParaEdicion(userId, nombre, email, rol);

            UserFormDialog dialog = new UserFormDialog(this, usuarioEditar, () -> cargarUsuarios());
            dialog.setVisible(true);
        });

        eliminarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tableUsers.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(
                            AdminForm.this,
                            "Seleccioná primero un usuario de la lista.",
                            "Sin selección",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                String nombre = String.valueOf(tableUsers.getValueAt(fila, 1));
                String email = String.valueOf(tableUsers.getValueAt(fila, 2));

                int confirmar = JOptionPane.showConfirmDialog(
                        AdminForm.this,
                        "¿Estás seguro de eliminar al usuario \"" + nombre + "\"?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmar == JOptionPane.YES_OPTION) {
                    String resultado = adminService.eliminarUsuario(email);
                    JOptionPane.showMessageDialog(
                            AdminForm.this,
                            resultado,
                            "Eliminar Usuario",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    cargarUsuarios();
                }
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StartForm();
            }
        });
    }

    private Usuario crearUsuarioParaEdicion(String id, String nombre, String email, String rolStr) {
        // Crear el tipo correcto de usuario según el rol
        return switch (rolStr) {
            case "EDITOR" -> new bll.usuarios.Editor(id, nombre, email);
            case "ADMIN" -> new bll.usuarios.Admin(id, nombre, email);
            default -> new bll.usuarios.Escritor(id, nombre, email);
        };
    }
}

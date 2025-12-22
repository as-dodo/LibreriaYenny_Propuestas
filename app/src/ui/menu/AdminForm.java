package ui.menu;

import bll.services.AdminService;
import bll.usuarios.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
    private JTabbedPane tabAdmin;
    private JTabbedPane tabbedPane1;
    private JTable table1;
    private JButton actualizarButton;
    private JButton volverButton;
    private JTable tableLibros;
    private JPanel panelLibrosAction;
    private JButton actualizarPrecioButton;

    private final AdminService adminService = new AdminService();

    public AdminForm() {
        setContentPane(rootPanel);
        setTitle("Administrador");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        construirTabs();
        inicializarTablaUsuarios();
        inicializarTablaLibros();
        inicializarEventosUsuarios();
        inicializarEventosLibros();
        cargarUsuarios();
        cargarLibros();

        setVisible(true);
    }

    private void construirTabs() {
        tabAdmin = new JTabbedPane();

        JPanel usuariosPanel = new JPanel(new BorderLayout());
        usuariosPanel.add(panelAdmin, BorderLayout.CENTER);
        usuariosPanel.add(panelAdminAction, BorderLayout.SOUTH);

        tableLibros = new JTable();
        panelLibrosAction = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actualizarPrecioButton = new JButton("Editar precio");
        panelLibrosAction.add(actualizarPrecioButton);

        JPanel librosPanel = new JPanel(new BorderLayout());
        librosPanel.add(new JScrollPane(tableLibros), BorderLayout.CENTER);
        librosPanel.add(panelLibrosAction, BorderLayout.SOUTH);

        rootPanel.removeAll();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(tabAdmin, BorderLayout.CENTER);

        tabAdmin.addTab("Usuarios", usuariosPanel);
        tabAdmin.addTab("Libros aprobados", librosPanel);
    }

    private void inicializarTablaUsuarios() {
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
        tableUsers.getColumnModel().getColumn(0).setMinWidth(0);
        tableUsers.getColumnModel().getColumn(0).setMaxWidth(0);
        tableUsers.getColumnModel().getColumn(0).setWidth(0);
    }

    private void inicializarTablaLibros() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Titulo", "Autor", "Precio", "Tirada", "Estado", "Comercializacion"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLibros.setModel(model);
        tableLibros.setRowHeight(26);
        tableLibros.getColumnModel().getColumn(0).setMinWidth(0);
        tableLibros.getColumnModel().getColumn(0).setMaxWidth(0);
        tableLibros.getColumnModel().getColumn(0).setWidth(0);
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

    private void cargarLibros() {
        DefaultTableModel model = (DefaultTableModel) tableLibros.getModel();
        model.setRowCount(0);

        var libros = adminService.obtenerLibrosAprobados();
        for (var libro : libros) {
            model.addRow(new Object[]{
                    libro.getPropuestaId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    String.format("%.2f", libro.getPrecio()),
                    libro.getTiradaInicial(),
                    libro.getEstadoPropuesta(),
                    libro.getEstadoComercializacion()
            });
        }
    }

    private void inicializarEventosUsuarios() {

        crearUsuarioButton.addActionListener(e -> {
            UserFormDialog dialog = new UserFormDialog(this, () -> cargarUsuarios());
            dialog.setVisible(true);
        });

        modificarUsuarioButton.addActionListener(e -> {
            int fila = tableUsers.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona primero un usuario de la lista.",
                        "Sin seleccion",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String userId = String.valueOf(tableUsers.getValueAt(fila, 0));
            String nombre = String.valueOf(tableUsers.getValueAt(fila, 1));
            String email = String.valueOf(tableUsers.getValueAt(fila, 2));
            String rol = String.valueOf(tableUsers.getValueAt(fila, 3));

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
                            "Selecciona primero un usuario de la lista.",
                            "Sin seleccion",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                String nombre = String.valueOf(tableUsers.getValueAt(fila, 1));
                String email = String.valueOf(tableUsers.getValueAt(fila, 2));

                int confirmar = JOptionPane.showConfirmDialog(
                        AdminForm.this,
                        "¿Estas seguro de eliminar al usuario \"" + nombre + "\"?",
                        "Confirmar eliminacion",
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

    private void inicializarEventosLibros() {
        actualizarPrecioButton.addActionListener(e -> actualizarPrecioSeleccionado());
    }

    private void actualizarPrecioSeleccionado() {
        int fila = tableLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona primero un libro de la lista.",
                    "Sin seleccion",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int propuestaId = (int) tableLibros.getValueAt(fila, 0);
        String titulo = String.valueOf(tableLibros.getValueAt(fila, 1));
        String precioActual = String.valueOf(tableLibros.getValueAt(fila, 3));

        String nuevoPrecio = JOptionPane.showInputDialog(
                this,
                "Nuevo precio para \"" + titulo + "\":",
                precioActual
        );
        if (nuevoPrecio == null) return;

        String resultado = adminService.actualizarPrecio(String.valueOf(propuestaId), nuevoPrecio);
        JOptionPane.showMessageDialog(
                this,
                resultado,
                "Editar precio",
                JOptionPane.INFORMATION_MESSAGE
        );
        cargarLibros();
    }

    private Usuario crearUsuarioParaEdicion(String id, String nombre, String email, String rolStr) {
        return switch (rolStr) {
            case "EDITOR" -> new bll.usuarios.Editor(id, nombre, email);
            case "ADMIN" -> new bll.usuarios.Admin(id, nombre, email);
            default -> new bll.usuarios.Escritor(id, nombre, email);
        };
    }
}


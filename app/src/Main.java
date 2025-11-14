import dll.Conexion;
import ui.menu.MenuPrincipal;
import ui.menu.StartForm;
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conexion = Conexion.getInstance().getConnection();
        if (conexion != null) {
            System.out.println("Conexión lista para usar!");
        }
        SwingUtilities.invokeLater(StartForm::new);

//        MenuPrincipal start = new MenuPrincipal();
//        start.run();
    }
}
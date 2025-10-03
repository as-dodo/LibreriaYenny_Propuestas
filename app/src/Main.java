import conexion.Conexion;
import menu.MenuPrincipal;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conexion = Conexion.getInstance().getConnection();
        if (conexion != null) {
            System.out.println("Conexión lista para usar!");
        }
        MenuPrincipal start = new MenuPrincipal();
        start.run();
    }
}
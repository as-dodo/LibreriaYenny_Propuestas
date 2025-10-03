package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/libreria_yenny";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Conexion instance;
    private Connection connection;

    private Conexion() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Se conectó");
        } catch (SQLException e) {
            System.out.println("❌ No se conectó: " + e.getMessage());
        }
    }

    public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

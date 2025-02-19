package controlestudios.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Ejecutar script SQL desde un archivo
            String sql = new String(
                    DatabaseInitializer.class.getResourceAsStream("/init.sql").readAllBytes()
            );
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
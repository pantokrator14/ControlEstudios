package controlestudios.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        String sqlResourcePath = "/init.sql"; // Ajusta según tu estructura

        try (InputStream is = DatabaseInitializer.class.getResourceAsStream(sqlResourcePath)) {

            if (is == null) {
                throw new IOException("¡Archivo no encontrado: " + sqlResourcePath);
            }

            // Leer el script SQL
            String sql = new String(is.readAllBytes()); // Java 9+
            // Usa el código alternativo si usas Java 8

            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate(sql);
                System.out.println("¡Base de datos inicializada correctamente!");

            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
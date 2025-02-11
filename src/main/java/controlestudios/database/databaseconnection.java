package controlestudios.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:control_estudios.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
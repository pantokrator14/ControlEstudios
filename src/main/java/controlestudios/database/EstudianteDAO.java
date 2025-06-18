package controlestudios.database;

import controlestudios.models.Estudiante;
import controlestudios.models.Materia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    // Guardar un estudiante
    public void guardarEstudiante(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (nombreCompleto, cedula, fechaNacimiento, seccion, grado) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estudiante.getNombreCompleto());
            pstmt.setString(2, estudiante.getCedula());
            pstmt.setDate(3, Date.valueOf(estudiante.getFechaNacimiento())); // Conversión a java.sql.Date
            pstmt.setString(4, estudiante.getSeccion());
            pstmt.setInt(5, estudiante.getGrado());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Actualizar estudiante
    public void actualizarEstudiante(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET nombreCompleto = ?, cedula = ?, fechaNacimiento = ?, " +
                "seccion = ?, grado = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombreCompleto());
            pstmt.setDate(2, Date.valueOf(estudiante.getFechaNacimiento())); // Corrección aquí
            pstmt.setString(3, estudiante.getCedula());
            pstmt.setString(4, estudiante.getSeccion());
            pstmt.setInt(5, estudiante.getGrado());
            pstmt.setInt(6, estudiante.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar estudiante: " + e.getMessage());
        }
    }

    // Obtener todos los estudiantes
    public ObservableList<Estudiante> obtenerTodosEstudiantes() {
        String sql = "SELECT * FROM estudiantes";
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getString("nombreCompleto"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("cedula"),
                        rs.getString("seccion"),
                        rs.getInt("grado")
                );
                estudiante.setId(rs.getInt("id"));
                estudiantes.add(estudiante);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener estudiantes: " + e.getMessage());
        }
        return estudiantes;
    }

    // Eliminar un estudiante por ID
    public void eliminarEstudiante(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
        }
    }

    public Estudiante buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM estudiantes WHERE cedula = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setId(rs.getInt("id"));
                estudiante.setNombreCompleto(rs.getString("nombre_completo"));
                estudiante.setCedula(rs.getString("cedula"));

                Date fechaSql = rs.getDate("fecha_nacimiento"); // Obtener como Date
                estudiante.setFechaNacimiento(fechaSql.toLocalDate()); // Convertir a LocalDate

                estudiante.setSeccion(rs.getString("seccion"));
                estudiante.setGrado(rs.getInt("grado"));
                return estudiante;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
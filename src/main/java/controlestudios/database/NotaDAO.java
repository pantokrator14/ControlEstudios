package controlestudios.database;

import controlestudios.models.Nota;
import controlestudios.models.Estudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotaDAO {

    // Guardar una nota
    public void guardarNota(Nota nota) {
        String sql = "INSERT INTO notas (estudiante_id, materia_id, calificacion) VALUES (?, ?, ?)";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nota.getIdEstudiante());
            pstmt.setInt(2, nota.getIdMateria());
            pstmt.setDouble(3, nota.getValor());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar nota: " + e.getMessage());
        }
    }

    // Obtener todas las notas de un estudiante
    public ObservableList<Nota> obtenerNotasPorEstudiante(int estudianteId) {
        String query = "SELECT n.*, m.nombre as nombre_materia FROM notas n "
                + "JOIN materias m ON n.materia_id = m.id "
                + "WHERE n.estudiante_id = ?";
        ObservableList<Nota> notas = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, estudianteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Nota nota = new Nota();
                nota.setId(rs.getInt("id"));
                nota.setValor(rs.getDouble("calificacion")); // Usar "calificacion"
                nota.setNombreMateria(rs.getString("nombre_materia"));
                notas.add(nota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notas;
    }

    // Actualizar nota
    public void actualizarNota(Nota nota) {
        String sql = "UPDATE notas SET calificacion = ? WHERE id = ?";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nota.getValor());
            pstmt.setInt(2, nota.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar nota: " + e.getMessage());
        }
    }

    // Eliminar nota
    public void eliminarNota(int id) {
        String sql = "DELETE FROM notas WHERE id = ?";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar nota: " + e.getMessage());
        }
    }

    public Map<String, Double> getPromediosPorMateria(String cedulaEstudiante) {
        Map<String, Double> promedios = new HashMap<>();
        String query = "SELECT m.nombre, AVG(n.nota) as promedio "
                + "FROM notas n "
                + "JOIN materias m ON n.id_materia = m.id "
                + "WHERE n.id_estudiante = (SELECT id FROM estudiantes WHERE cedula = ?) "
                + "GROUP BY m.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, cedulaEstudiante);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String materia = rs.getString("nombre");
                double promedio = rs.getDouble("promedio");
                promedios.put(materia, Math.round(promedio * 100.0) / 100.0);
            }

        } catch (SQLException e) {
            System.err.println("Error calculando promedios: " + e.getMessage());
        }
        return promedios;
    }

    public double getPromedioGeneral(String cedulaEstudiante) {
        String query = "SELECT AVG(nota) as promedio_general "
                + "FROM notas "
                + "WHERE id_estudiante = (SELECT id FROM estudiantes WHERE cedula = ?)";
        double promedio = 0.0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, cedulaEstudiante);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                promedio = rs.getDouble("promedio_general");
            }

        } catch (SQLException e) {
            System.err.println("Error calculando promedio general: " + e.getMessage());
        }
        return Math.round(promedio * 100.0) / 100.0;
    }
}
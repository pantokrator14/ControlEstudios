package controlestudios.database;

import controlestudios.models.Materia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MateriaDAO {

    // Guardar materia
    public void guardarMateria(Materia materia) {
        String sql = "INSERT INTO materias (nombre, descripcion, profesor, grado) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getDescripcion());
            pstmt.setString(3, materia.getProfesor());
            pstmt.setInt(4, materia.getGrado());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar materia: " + e.getMessage());
        }
    }

    // Actualizar materia
    public void actualizarMateria(Materia materia) {
        String sql = "UPDATE materias SET nombre = ?, descripcion = ?, profesor = ?," +
                "grado = ? WHERE id = ?";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getDescripcion());
            pstmt.setString(3, materia.getProfesor());
            pstmt.setInt(4, materia.getGrado());
            pstmt.setInt(5, materia.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar materia: " + e.getMessage());
        }
    }

    // Eliminar materia
    public void eliminarMateria(int id) {
        String sql = "DELETE FROM materias WHERE id = ?";

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar materia: " + e.getMessage());
        }
    }

    // Obtener todas las materias
    public ObservableList<Materia> obtenerTodasMaterias() {
        String sql = "SELECT * FROM materias";
        ObservableList<Materia> materias = FXCollections.observableArrayList();

        try (Connection conn = controlestudios.database.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Materia materia = new Materia(
                        rs.getString("nombre"),
                        rs.getString("profesor"),
                        rs.getString("descripcion"),
                        rs.getInt("grado")
                );
                materia.setId(rs.getInt("id"));
                materias.add(materia);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener materias: " + e.getMessage());
        }
        return materias;
    }

    public ObservableList<Materia> obtenerPorGrado(int grado) {
        String sql = "SELECT * FROM materias WHERE grado = ?";
        ObservableList<Materia> materias = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grado);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Materia materia = new Materia(
                        rs.getString("nombre"),
                        rs.getString("profesor"),
                        rs.getString("descripcion"),
                        rs.getInt("grado")
                );
                materia.setId(rs.getInt("id"));
                materias.add(materia);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener materias por grado: " + e.getMessage());
        }
        return materias;
    }

}
package controlestudios.database;

import controlestudios.models.Materia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    // Guardar materia
    public void guardarMateria(Materia materia) {
        String sql = "INSERT INTO materias (nombre, descripcion, nombre_profesor) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getDescripcion());
            pstmt.setString(3, materia.getNombreProfesor());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar materia: " + e.getMessage());
        }
    }

    // Actualizar materia
    public void actualizarMateria(Materia materia) {
        String sql = "UPDATE materias SET nombre = ?, descripcion = ?, nombre_profesor = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getDescripcion());
            pstmt.setString(3, materia.getNombreProfesor());
            pstmt.setInt(4, materia.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar materia: " + e.getMessage());
        }
    }

    // Eliminar materia
    public void eliminarMateria(int id) {
        String sql = "DELETE FROM materias WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar materia: " + e.getMessage());
        }
    }

    // Obtener todas las materias
    public List<Materia> obtenerTodasMaterias() {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT * FROM materias";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Materia materia = new Materia(
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("nombre_profesor")
                );
                materia.setId(rs.getInt("id"));
                materias.add(materia);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener materias: " + e.getMessage());
        }
        return materias;
    }

}
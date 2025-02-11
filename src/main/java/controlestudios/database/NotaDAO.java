package controlestudios.database;

import controlestudios.models.Nota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO {

    // Guardar una nota
    public void guardarNota(Nota nota) {
        String sql = "INSERT INTO notas (estudiante_id, materia_id, calificacion) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nota.getEstudianteId());
            pstmt.setInt(2, nota.getMateriaId());
            pstmt.setDouble(3, nota.getCalificacion());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar nota: " + e.getMessage());
        }
    }

    // Obtener todas las notas de un estudiante
    public List<Nota> obtenerNotasPorEstudiante(int estudianteId) {
        List<Nota> notas = new ArrayList<>();
        String sql = "SELECT * FROM notas WHERE estudiante_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, estudianteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Nota nota = new Nota(
                            rs.getInt("estudiante_id"),
                            rs.getInt("materia_id"),
                            rs.getDouble("calificacion")
                    );
                    nota.setId(rs.getInt("id"));
                    notas.add(nota);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener notas: " + e.getMessage());
        }
        return notas;
    }

    // Actualizar nota
    public void actualizarNota(Nota nota) {
        String sql = "UPDATE notas SET calificacion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nota.getCalificacion());
            pstmt.setInt(2, nota.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar nota: " + e.getMessage());
        }
    }

    // Eliminar nota
    public void eliminarNota(int id) {
        String sql = "DELETE FROM notas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar nota: " + e.getMessage());
        }
    }
}
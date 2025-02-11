package controlestudios.database;

import controlestudios.models.Estudiante;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    // Guardar estudiante
    public void guardarEstudiante(Estudiante estudiante) { ... } // Mantén el código anterior

    // Actualizar estudiante
    public void actualizarEstudiante(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET nombre_completo = ?, fecha_nacimiento = ?, cedula = ?, seccion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombreCompleto());
            pstmt.setString(2, estudiante.getFechaNacimiento().toString());
            pstmt.setString(3, estudiante.getCedula());
            pstmt.setString(4, estudiante.getSeccion());
            pstmt.setInt(5, estudiante.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar estudiante: " + e.getMessage());
        }
    }

    // Obtener todos los estudiantes
    public List<Estudiante> obtenerTodosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getString("nombre_completo"),
                        LocalDate.parse(rs.getString("fecha_nacimiento")),
                        rs.getString("cedula"),
                        rs.getString("seccion")
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

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
        }
    }
}
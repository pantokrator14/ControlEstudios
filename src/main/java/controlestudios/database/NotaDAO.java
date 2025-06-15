package controlestudios.database;

import controlestudios.models.Nota;
import controlestudios.utils.PeriodoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotaDAO {

    // ==================== OPERACIONES CRUD ====================

    public void guardarNota(Nota nota) {
        String sql = "INSERT INTO notas (estudiante_id, materia_id, calificacion, fecha_registro, anio_escolar) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nota.getIdEstudiante());
            pstmt.setInt(2, nota.getIdMateria());
            pstmt.setDouble(3, nota.getValor());
            pstmt.setDate(4, Date.valueOf(nota.getFechaRegistro()));
            pstmt.setInt(5, nota.getAnioEscolar());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar nota: " + e.getMessage());
        }
    }

    public void actualizarNota(Nota nota) {
        String sql = "UPDATE notas SET calificacion = ?, fecha_registro = ?, anio_escolar = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nota.getValor());
            pstmt.setDate(2, Date.valueOf(nota.getFechaRegistro()));
            pstmt.setInt(3, nota.getAnioEscolar());
            pstmt.setInt(4, nota.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar nota: " + e.getMessage());
        }
    }

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

    // ==================== CONSULTAS ====================

    public ObservableList<Nota> obtenerNotasPorEstudiante(int estudianteId) {
        String query = "SELECT n.*, m.nombre as nombre_materia " +
                "FROM notas n " +
                "JOIN materias m ON n.materia_id = m.id " +
                "WHERE n.estudiante_id = ?";

        ObservableList<Nota> notas = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, estudianteId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                notas.add(mapearNota(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener notas: " + e.getMessage());
        }
        return notas;
    }

    public ObservableList<Nota> obtenerPorMomento(int estudianteId, int momento, int anioEscolar) {
        LocalDate[] fechas = PeriodoUtil.obtenerFechasMomento(momento, anioEscolar);
        if (fechas == null) return FXCollections.observableArrayList();

        String query = "SELECT n.*, m.nombre as nombre_materia " +
                "FROM notas n " +
                "JOIN materias m ON n.materia_id = m.id " +
                "WHERE n.estudiante_id = ? " +
                "AND n.anio_escolar = ? " +
                "AND n.fecha_registro BETWEEN ? AND ?";

        ObservableList<Nota> notas = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, estudianteId);
            pstmt.setInt(2, anioEscolar);
            pstmt.setDate(3, Date.valueOf(fechas[0]));
            pstmt.setDate(4, Date.valueOf(fechas[1]));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                notas.add(mapearNota(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener notas por momento: " + e.getMessage());
        }
        return notas;
    }

    public List<Integer> obtenerAniosEscolares() {
        String sql = "SELECT DISTINCT anio_escolar FROM notas ORDER BY anio_escolar DESC";
        List<Integer> anios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                anios.add(rs.getInt("anio_escolar"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener años escolares: " + e.getMessage());
        }
        return anios;
    }

    // ==================== CÁLCULO DE PROMEDIOS ====================

    public double getPromedioGeneral(int estudianteId, int anioEscolar) {
        String query = "SELECT AVG(calificacion) as promedio_general " +
                "FROM notas " +
                "WHERE estudiante_id = ? " +
                "AND anio_escolar = ?";
        double promedio = 0.0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, estudianteId);
            pstmt.setInt(2, anioEscolar);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                promedio = rs.getDouble("promedio_general");
            }

        } catch (SQLException e) {
            System.err.println("Error calculando promedio general: " + e.getMessage());
        }
        return Math.round(promedio * 100.0) / 100.0;  // Redondear a 2 decimales
    }

    public Map<String, Double> getPromediosPorMateria(int estudianteId, int anioEscolar) {
        String query = "SELECT m.nombre, AVG(n.calificacion) as promedio " +
                "FROM notas n " +
                "JOIN materias m ON n.materia_id = m.id " +
                "WHERE n.estudiante_id = ? " +
                "AND n.anio_escolar = ? " +
                "GROUP BY m.nombre";

        Map<String, Double> promedios = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, estudianteId);
            pstmt.setInt(2, anioEscolar);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String materia = rs.getString("nombre");
                double promedio = rs.getDouble("promedio");
                promedios.put(materia, Math.round(promedio * 100.0) / 100.0);
            }

        } catch (SQLException e) {
            System.err.println("Error calculando promedios por materia: " + e.getMessage());
        }
        return promedios;
    }

    // ==================== UTILIDADES ====================

    private Nota mapearNota(ResultSet rs) throws SQLException {
        Nota nota = new Nota();
        nota.setId(rs.getInt("id"));
        nota.setIdEstudiante(rs.getInt("estudiante_id"));
        nota.setIdMateria(rs.getInt("materia_id"));
        nota.setValor(rs.getDouble("calificacion"));
        nota.setNombreMateria(rs.getString("nombre_materia"));
        nota.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        nota.setAnioEscolar(rs.getInt("anio_escolar"));
        return nota;
    }
}
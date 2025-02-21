package controlestudios.controllers;


import controlestudios.models.Materia;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import controlestudios.models.Estudiante;
import controlestudios.database.EstudianteDAO;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class EstudianteController {
    //Sidebar
    @FXML
    private VBox sidebar;



    @FXML
    private void handleMaterias() {
        // Cargar interfaz de materias (pendiente)
    }

    @FXML
    private void handleEstudiantes() {
        // Cargar interfaz de estudiantes (pendiente)
    }

    @FXML
    private void handleNotas() {
        // Cargar interfaz de notas (pendiente)
    }

    @FXML
    private void handleSalir() {
        // Cerrar sesión y volver al login
        Stage stage = (Stage) sidebar.getScene().getWindow();
        stage.close();
    }

    //Contenido
    @FXML private TableView<Estudiante> tablaEstudiantes;
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    @FXML
    public void initialize() {
        cargarDatos();
        configurarAccionesTabla();
    }

    private void cargarDatos() {
        ObservableList<Estudiante> estudiantes = estudianteDAO.obtenerTodosEstudiantes();
        tablaEstudiantes.setItems(estudiantes);
    }

    private void configurarAccionesTabla() {
        // Implementar botones de editar/eliminar (similar a MateriaController)
    }

    @FXML
    private void handleAgregarEstudiante() {
        // Lógica para abrir formulario
    }

}

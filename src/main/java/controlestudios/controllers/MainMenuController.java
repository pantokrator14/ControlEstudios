package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class MainMenuController {

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
        stage.close(); // Cierra la ventana actual
        // Aquí puedes cargar nuevamente el login si lo necesitas
    }
}
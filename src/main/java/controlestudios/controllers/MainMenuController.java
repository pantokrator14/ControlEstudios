package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    @FXML
    private VBox sidebar;



    @FXML
    private void handleMaterias() {
        cargarVista("/views/materias.fxml");
    }

    @FXML
    private void handleEstudiantes() {
        cargarVista("/views/estudiantes.fxml");
    }

    @FXML
    private void handleNotas() {
        cargarVista("/views/notas.fxml");
    }

    @FXML
    private void handleSalir() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
            Stage stage = new Stage();
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método genérico para cargar vistas
    private void cargarVista(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Cerrar la ventana actual
            ((Stage) sidebar.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar el login
    private void cargarLogin() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
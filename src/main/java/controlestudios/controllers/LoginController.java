package controlestudios.controllers;

import controlestudios.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName()); // Logger nativo

    // Componentes de UI (sin cambios)
    @FXML private ToggleButton themeToggle;
    @FXML private StackPane rootPane;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Campos vacíos", "Usuario y contraseña son obligatorios.");
            logger.warning("Intento de login con campos vacíos"); // Nivel WARNING
            return;
        }

        if ("admin".equals(username) && "admin".equals(password)) {
            logger.info("Login exitoso para usuario: " + username); // Nivel INFO
            cargarMenuPrincipal();
        } else {
            String mensajeError = "Credenciales inválidas para usuario: " + username;
            logger.log(Level.SEVERE, mensajeError); // Nivel SEVERE
            mostrarError("Credenciales incorrectas", mensajeError);
        }
    }

    private void cargarMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);

            logger.fine("Menú principal cargado exitosamente"); // Nivel FINE

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error crítico al cargar el menú principal", e); // Log detallado
            mostrarError("Error fatal", "No se pudo inicializar la interfaz principal");
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
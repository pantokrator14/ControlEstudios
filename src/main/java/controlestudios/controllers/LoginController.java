package controlestudios.controllers;

import controlestudios.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML private ToggleButton themeToggle;
    @FXML private StackPane rootPane;


    /*Login */
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Campos vacíos", "Usuario y contraseña son obligatorios.");
            return;
        }

        if (username.equals("admin") && password.equals("admin")) {
            cargarMenuPrincipal();
        } else {
            mostrarError("Credenciales incorrectas", "Usuario o contraseña inválidos.");
        }
    }

    private void cargarMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual y reutilizarla
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true); // Mantener maximizada
        } catch (Exception e) {
            logger.error("Error al cargar el menú principal", e);
            mostrarError("Error", "No se pudo cargar el menú principal.");
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
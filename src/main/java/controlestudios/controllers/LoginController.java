package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim(); // Elimina espacios en blanco
        String password = passwordField.getText().trim();

        // Validación 1: Campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Campos vacíos", "Usuario y contraseña son obligatorios.");
            limpiarCampos();
            return;
        }

        // Validación 2: Credenciales correctas
        if (username.equals("admin") && password.equals("admin")) {
            cargarMenuPrincipal();
        } else {
            mostrarError("Credenciales incorrectas", "Usuario o contraseña inválidos.");
            limpiarCampos();
        }
    }

    private void cargarMenuPrincipal() {
        try {
            // Cargar el archivo FXML del menú principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual (Stage)
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Crear una nueva escena con el menú principal
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menú Principal");
            stage.show();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo cargar el menú principal.");
            e.printStackTrace();
        }
    }

    // Método para limpiar los campos después de un intento fallido
    private void limpiarCampos() {
        usernameField.clear();
        passwordField.clear();
        usernameField.requestFocus(); // Enfoca el campo de usuario nuevamente
    }

    // Método para mostrar errores (mejorado)
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Login");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
package controlestudios;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carga el FXML con verificación de null
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/views/login.fxml"),
                            "No se encontró el archivo FXML: /views/login.fxml"
                    )
            );

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Control de Estudios");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException | NullPointerException e) {
            // Logging profesional con SLF4J
            logger.error("Error crítico al iniciar la aplicación", e);
            showFatalErrorAlert();
        }
    }

    private void showFatalErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Fatal");
        alert.setHeaderText("No se pudo iniciar la aplicación");
        alert.setContentText("Por favor, reinstale el software o contacte al soporte.");
        alert.showAndWait();
        System.exit(1); // Cierra la aplicación con código de error
    }

    public static void main(String[] args) {
        launch(args);
    }
}
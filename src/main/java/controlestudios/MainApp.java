package controlestudios;

import controlestudios.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class MainApp extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseInitializer.initialize();
            Parent root = loadFXML("/views/login.fxml");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Control de Estudios");
            primaryStage.show();
        } catch (Exception e) {
            LOG.error("Error crítico al iniciar la aplicación", e);
            showFatalError();
        }
    }

    public static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
        return loader.load();
    }

    public static Scene loadScene(String fxmlPath) throws IOException {
        return new Scene(loadFXML(fxmlPath));
    }

    private void showFatalError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Fatal");
        alert.setHeaderText("La aplicación no puede iniciar");
        alert.setContentText("Por favor, contacte al soporte técnico.");
        alert.showAndWait();
        System.exit(1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
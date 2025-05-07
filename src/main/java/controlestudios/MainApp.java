package controlestudios;

import controlestudios.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger LOG = Logger.getLogger(MainApp.class.getName()); // Logger nativo de Java

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
            LOG.severe("Error crítico al iniciar la aplicación: " + e.getMessage()); // Log nivel SEVERE
            e.printStackTrace(); // Traza completa en consola
            showFatalError();
        }
    }

    // Metodo para cargar FXML (sin cambios)
    public static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
        return loader.load();
    }

    // Metodo para cargar escenas (sin cambios)
    public static Scene loadScene(String fxmlPath) throws IOException {
        return new Scene(loadFXML(fxmlPath));
    }

    // Metodo para mostrar error fatal (sin cambios)
    private void showFatalError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Fatal");
        alert.setHeaderText("La aplicación no puede iniciar");
        alert.setContentText("Por favor, contacte al soporte técnico.");
        alert.showAndWait();
        System.exit(1);
    }

    // Metodo main con configuración de logging
    public static void main(String[] args) {
        // Configurar logging antes de iniciar la app
        try {
            InputStream is = MainApp.class.getResourceAsStream("/logging.properties");
            if (is == null) {
                throw new IOException("Archivo logging.properties no encontrado");
            }
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            System.err.println("Error cargando logging.properties: " + e.getMessage());
            e.printStackTrace();
        }

        launch(args);
    }
}
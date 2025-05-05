package controlestudios.controllers;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import controlestudios.models.Estudiante;
import controlestudios.database.EstudianteDAO;
import controlestudios.controllers.EstudianteFormController;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class EstudianteController {
    //Sidebar
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
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Contenido
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colCedula;
    @FXML private TableColumn<Estudiante, LocalDate> colFechaNacimiento;
    @FXML private TableColumn<Estudiante, String> colSeccion;
    @FXML private TableColumn<Estudiante, Void> colAcciones;

    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();
        configurarAccionesTabla();
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colSeccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
    }

    private void cargarDatos() {
        ObservableList<Estudiante> estudiantes = estudianteDAO.obtenerTodosEstudiantes();
        tablaEstudiantes.setItems(estudiantes);
    }

    private void configurarAccionesTabla() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();

            {
                // Ícono Editar
                FontIcon iconoEditar = new FontIcon("fas-pencil-alt");
                iconoEditar.setIconSize(16);
                btnEditar.setGraphic(iconoEditar);
                btnEditar.getStyleClass().addAll("action-button", "edit-button");

                // Ícono Eliminar
                FontIcon iconoEliminar = new FontIcon("fas-trash-alt");
                iconoEliminar.setIconSize(16);
                btnEliminar.setGraphic(iconoEliminar);
                btnEliminar.getStyleClass().addAll("action-button", "delete-button");

                // Eventos
                btnEditar.setOnAction(e -> {
                    Estudiante estudiante = getTableView().getItems().get(getIndex());
                    editarEstudiante(estudiante);
                });

                btnEliminar.setOnAction(e -> {
                    Estudiante estudiante = getTableView().getItems().get(getIndex());
                    eliminarEstudiante(estudiante);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnEditar, btnEliminar));
            }
        });
    }

    @FXML
    private void handleAgregarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/estudiante_form.fxml"));
            Parent root = loader.load();

            EstudianteFormController controller = loader.getController();
            controller.setDialogStage(new Stage());
            controller.setEstudiante(null); // Modo creación

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isGuardado()) {
                estudianteDAO.guardarEstudiante(controller.getEstudiante());
                cargarDatos();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editarEstudiante(Estudiante estudiante) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/estudiante_form.fxml"));
            Parent root = loader.load();

            EstudianteFormController controller = loader.getController();
            controller.setDialogStage(new Stage());
            controller.setEstudiante(estudiante); // Modo edición

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isGuardado()) {
                estudianteDAO.actualizarEstudiante(controller.getEstudiante());
                cargarDatos();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarEstudiante(Estudiante estudiante) {
        if (mostrarConfirmacion("¿Eliminar al estudiante " + estudiante.getNombreCompleto() + "?")) {
            estudianteDAO.eliminarEstudiante(estudiante.getId());
            cargarDatos();
        }
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private final BooleanProperty tablaVacia = new SimpleBooleanProperty();


    // Getter para FXML (requerido)
    public BooleanProperty tablaVaciaProperty() {
        return tablaVacia;
    }

    // Getter tradicional (opcional)
    public boolean isTablaVacia() {
        return tablaVacia.get();
    }
}

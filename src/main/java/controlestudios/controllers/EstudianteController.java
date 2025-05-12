package controlestudios.controllers;

import controlestudios.database.EstudianteDAO;
import controlestudios.models.Estudiante;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstudianteController {
    private static final Logger LOG = Logger.getLogger(EstudianteController.class.getName()); // Logger nativo

    // Sidebar y componentes FXML (sin cambios)
    @FXML private VBox sidebar;
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colCedula;
    @FXML private TableColumn<Estudiante, LocalDate> colFechaNacimiento;
    @FXML private TableColumn<Estudiante, String> colSeccion;
    @FXML private TableColumn<Estudiante, Void> colAcciones;

    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    // ==================== MÉTODOS DE NAVEGACIÓN ====================
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
            LOG.log(Level.SEVERE, "Error al cargar la vista de login", e); // Loggeo profesional
        }
    }

    // ==================== LÓGICA DE CARGA DE VISTAS ====================
    private void cargarVista(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
            ((Stage) sidebar.getScene().getWindow()).close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error al cargar la vista: " + fxmlPath, e);
        }
    }

    // ==================== LÓGICA PRINCIPAL ====================
    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();
        configurarAccionesTabla();
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colFechaNacimiento.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFechaNacimiento())
        );
        colSeccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
    }

    private void cargarDatos() {
        ObservableList<Estudiante> estudiantes = estudianteDAO.obtenerTodosEstudiantes();
        tablaEstudiantes.setItems(estudiantes);
    }

    // ==================== ACCIONES DE LA TABLA ====================
    private void configurarAccionesTabla() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();

            {
                // Configuración de íconos
                FontIcon iconoEditar = new FontIcon("mdi-pencil");
                iconoEditar.setIconSize(16);
                btnEditar.setGraphic(iconoEditar);
                btnEditar.getStyleClass().addAll("action-button", "edit-button");

                FontIcon iconoEliminar = new FontIcon("mdi-delete");
                iconoEliminar.setIconSize(16);
                btnEliminar.setGraphic(iconoEliminar);
                btnEliminar.getStyleClass().addAll("action-button", "delete-button");

                // Eventos con manejo de errores
                btnEditar.setOnAction(e -> {
                    try {
                        Estudiante estudiante = getTableView().getItems().get(getIndex());
                        editarEstudiante(estudiante);
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "Error al editar estudiante", ex);
                    }
                });

                btnEliminar.setOnAction(e -> {
                    try {
                        Estudiante estudiante = getTableView().getItems().get(getIndex());
                        eliminarEstudiante(estudiante);
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "Error al eliminar estudiante", ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnEditar, btnEliminar));
            }
        });
    }

    // ==================== OPERACIONES CRUD ====================
    @FXML
    private void handleAgregarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/estudiante_form.fxml"));
            Parent root = loader.load();

            EstudianteFormController controller = loader.getController();
            controller.setDialogStage(new Stage());
            controller.setEstudiante(null);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isGuardado()) {
                estudianteDAO.guardarEstudiante(controller.getEstudiante());
                cargarDatos();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error al abrir formulario de estudiante", e);
        }
    }

    private void editarEstudiante(Estudiante estudiante) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/estudiante_form.fxml"));
            Parent root = loader.load();

            EstudianteFormController controller = loader.getController();
            controller.setDialogStage(new Stage());
            controller.setEstudiante(estudiante);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isGuardado()) {
                estudianteDAO.actualizarEstudiante(controller.getEstudiante());
                cargarDatos();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error al editar estudiante", e);
        }
    }

    private void eliminarEstudiante(Estudiante estudiante) {
        if (mostrarConfirmacion("¿Eliminar al estudiante " + estudiante.getNombreCompleto() + "?")) {
            try {
                estudianteDAO.eliminarEstudiante(estudiante.getId());
                cargarDatos();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error al eliminar estudiante", e);
                new Alert(Alert.AlertType.ERROR, "Error al eliminar el estudiante").show();
            }
        }
    }

    // ==================== UTILIDADES ====================
    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    // Propiedades observables (sin cambios)
    private final BooleanProperty tablaVacia = new SimpleBooleanProperty();
    public BooleanProperty tablaVaciaProperty() {
        return tablaVacia;
    }
    public boolean isTablaVacia() {
        return tablaVacia.get();
    }
}
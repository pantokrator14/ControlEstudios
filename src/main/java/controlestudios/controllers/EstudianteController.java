package controlestudios.controllers;


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

import java.io.IOException;
import java.time.LocalDate;

public class EstudianteController {
    //Sidebar
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
        stage.close();
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
        colAcciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Estudiante, Void> call(final TableColumn<Estudiante, Void> param) {
                return new TableCell<>() {
                    private final Button btnEditar = new Button();
                    private final Button btnEliminar = new Button();

                    {
                        // Configurar botón Editar
                        SVGPath iconoEditar = new SVGPath();
                        iconoEditar.setContent("M12 2l-5.5 9h11L12 2zm0 3.84L13.93 9h-3.87L12 5.84z");
                        btnEditar.setGraphic(iconoEditar);
                        btnEditar.getStyleClass().addAll("action-button", "edit-button");
                        btnEditar.setOnAction(e -> editarEstudiante(getTableView().getItems().get(getIndex())));

                        // Configurar botón Eliminar
                        SVGPath iconoEliminar = new SVGPath();
                        iconoEliminar.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                        btnEliminar.setGraphic(iconoEliminar);
                        btnEliminar.getStyleClass().addAll("action-button", "delete-button");
                        btnEliminar.setOnAction(e -> eliminarEstudiante(getTableView().getItems().get(getIndex())));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new HBox(10, btnEditar, btnEliminar));
                        }
                    }
                };
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
}

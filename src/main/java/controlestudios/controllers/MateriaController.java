package controlestudios.controllers;

import controlestudios.models.Estudiante;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import controlestudios.models.Materia;
import controlestudios.database.MateriaDAO;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MateriaController {

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
        // Cerrar ventana actual y volver al login
        Stage stage = (Stage) sidebar.getScene().getWindow();
        stage.close();
        cargarLogin();
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


    //Contenido
    @FXML
    private TableView<Materia> tablaMaterias;
    private final MateriaDAO materiaDAO = new MateriaDAO();

    @FXML private TableColumn<Materia, Void> colAcciones;

    @FXML
    public void initialize() {
        cargarDatos();
        configurarAccionesTabla();
    }

    private void cargarDatos() {
        ObservableList<Materia> materias = materiaDAO.obtenerTodasMaterias();
        tablaMaterias.setItems(materias); // Asignación correcta
    }

    private void configurarAccionesTabla() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();

            {
                // Configurar botón Editar
                SVGPath iconoEditar = new SVGPath();
                iconoEditar.setContent("M12 2l-5.5 9h11L12 2zm0 3.84L13.93 9h-3.87L12 5.84z");
                btnEditar.setGraphic(iconoEditar);
                btnEditar.getStyleClass().addAll("action-button", "edit-button");
                btnEditar.setOnAction(e -> {
                    Materia materia = (Materia) getTableView().getItems().get(getIndex());
                    editarMateria(materia);
                });

                // Configurar botón Eliminar
                SVGPath iconoEliminar = new SVGPath();
                iconoEliminar.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                btnEliminar.setGraphic(iconoEliminar);
                btnEliminar.getStyleClass().addAll("action-button", "delete-button");
                btnEliminar.setOnAction(e -> {
                    Materia materia = (Materia) getTableView().getItems().get(getIndex());
                    eliminarMateria(materia);
                });
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, btnEditar, btnEliminar));
                }
            }
        });
    }



    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }


    @FXML
    private void handleAgregarMateria() {
        mostrarFormularioMateria(null);
    }

    private void editarMateria(Materia materia) {
        mostrarFormularioMateria(materia);
    }

    private void mostrarFormularioMateria(Materia materia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/materia_form.fxml"));
            Parent root = loader.load();

            MateriaFormController controller = loader.getController();
            controller.setMateria(materia);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(materia == null ? "Nueva Materia" : "Editar Materia");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isGuardado()) {
                if (materia == null) {
                    materiaDAO.guardarMateria(controller.getMateria());
                } else {
                    materiaDAO.actualizarMateria(controller.getMateria());
                }
                cargarDatos();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarMateria(Materia materia) {
        if (mostrarConfirmacion("¿Eliminar materia?")) {
            materiaDAO.eliminarMateria(materia.getId());
            cargarDatos();
        }
    }
}
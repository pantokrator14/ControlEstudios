package controlestudios.controllers;

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

public class MateriaController {

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
    @FXML
    private TableView<Materia> tablaMaterias;
    private final MateriaDAO materiaDAO = new MateriaDAO();

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
        @SuppressWarnings("unchecked")
        TableColumn<Materia, Void> colAcciones = (TableColumn<Materia, Void>) tablaMaterias.getColumns().get(3);

        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = crearBotonEditar();
            private final Button btnEliminar = crearBotonEliminar();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Materia materia = getTableView().getItems().get(getIndex());
                    btnEditar.setOnAction(e -> editarMateria(materia));
                    btnEliminar.setOnAction(e -> eliminarMateria(materia));
                    setGraphic(new HBox(10, btnEditar, btnEliminar));
                }
            }
        });
    }

    private Button crearBotonEditar() {
        Button btn = new Button();
        SVGPath icono = new SVGPath();
        icono.setContent("M12 2l-5.5 9h11L12 2zm0 3.84L13.93 9h-3.87L12 5.84z");
        btn.getStyleClass().addAll("action-button", "edit-button");
        btn.setGraphic(icono);
        return btn;
    }

    private Button crearBotonEliminar() {
        Button btn = new Button();
        SVGPath icono = new SVGPath();
        icono.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        btn.getStyleClass().addAll("action-button", "delete-button");
        btn.setGraphic(icono);
        return btn;
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
package controlestudios.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import controlestudios.models.*;
import controlestudios.database.*;
import java.io.IOException;

public class NotaController {

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


    //contenido

    @FXML private StackPane contenidoDinamico;
    @FXML private TextField txtCedulaBusqueda;
    @FXML private TableView<Nota> tablaNotas;
    @FXML private TableColumn<Nota, String> colMateria;
    @FXML private TableColumn<Nota, Double> colNota;
    @FXML private TableColumn<Nota, Void> colAcciones;

    private final BooleanProperty modoBusqueda = new SimpleBooleanProperty(true);

    private Estudiante estudianteActual;
    private final NotaDAO notaDAO = new NotaDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarAccionesTabla();
    }

    private void configurarColumnas() {
        colMateria.setCellValueFactory(new PropertyValueFactory<>("nombreMateria"));
        colNota.setCellValueFactory(new PropertyValueFactory<>("valor"));
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
                btnEditar.setOnAction(e -> editarNota(getTableView().getItems().get(getIndex())));

                // Configurar botón Eliminar
                SVGPath iconoEliminar = new SVGPath();
                iconoEliminar.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                btnEliminar.setGraphic(iconoEliminar);
                btnEliminar.getStyleClass().addAll("action-button", "delete-button");
                btnEliminar.setOnAction(e -> eliminarNota(getTableView().getItems().get(getIndex())));
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
        });
    }

    @FXML
    private void handleBuscarEstudiante() {
        String cedula = txtCedulaBusqueda.getText().trim();
        if (cedula.isEmpty()) return;

        estudianteActual = estudianteDAO.buscarPorCedula(cedula);
        if (estudianteActual == null) {
            mostrarErrorEstudianteNoEncontrado();
        } else {
            setModoBusqueda(false); // Actualiza la propiedad
            cargarNotasEstudiante();
        }
    }

    private void cargarNotasEstudiante() {
        ObservableList<Nota> notas = notaDAO.obtenerNotasPorEstudiante(estudianteActual.getId());
        tablaNotas.setItems(notas);
    }

    private void mostrarErrorEstudianteNoEncontrado() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Estudiante no encontrado");
        alert.setContentText("No existe un estudiante con la cédula ingresada.");

        ButtonType btnIrAEstudiantes = new ButtonType("Registrar Estudiante", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(btnIrAEstudiantes);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == btnIrAEstudiantes) {
                navegarAEstudiantes();
            }
        });
    }

    private void navegarAEstudiantes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/estudiantes.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contenidoDinamico.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistrarNota() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nota_form.fxml"));
            Parent root = loader.load();

            NotaFormController controller = loader.getController();
            controller.setEstudiante(estudianteActual);
            controller.setMaterias(materiaDAO.obtenerTodasMaterias());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isGuardado()) {
                notaDAO.guardarNota(controller.getNota());
                cargarNotasEstudiante();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editarNota(Nota nota) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nota_form.fxml"));
            Parent root = loader.load();

            NotaFormController controller = loader.getController();
            controller.setNota(nota);
            controller.setMaterias(materiaDAO.obtenerTodasMaterias());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isGuardado()) {
                notaDAO.actualizarNota(controller.getNota());
                cargarNotasEstudiante();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarNota(Nota nota) {
        if (mostrarConfirmacion("¿Eliminar la nota de " + nota.getNombreMateria() + "?")) {
            notaDAO.eliminarNota(nota.getId());
            cargarNotasEstudiante();
        }
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    // Getter para la propiedad (requerido por FXML)
    public BooleanProperty modoBusquedaProperty() {
        return modoBusqueda;
    }

    // Getter tradicional
    public boolean isModoBusqueda() {
        return modoBusqueda.get();
    }

    // Setter
    public void setModoBusqueda(boolean modoBusqueda) {
        this.modoBusqueda.set(modoBusqueda);
    }


}
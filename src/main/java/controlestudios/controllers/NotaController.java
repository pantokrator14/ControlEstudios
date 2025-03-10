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
import controlestudios.models.*;
import controlestudios.database.*;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.Objects;

public class NotaController {

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
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Contenido

    //============= PROPERTIES =============
    private final BooleanProperty modoBusqueda = new SimpleBooleanProperty(true);
    private Estudiante estudianteActual;
    private final NotaDAO notaDAO = new NotaDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();

    //============= FXML COMPONENTS =============
    @FXML private StackPane contenidoDinamico;
    @FXML private TextField txtCedulaBusqueda;
    @FXML private VBox pantallaBusqueda;
    @FXML private VBox pantallaNotas;
    @FXML private TableView<Nota> tablaNotas;
    @FXML private TableColumn<Nota, String> colMateria;
    @FXML private TableColumn<Nota, Double> colNota;
    @FXML private TableColumn<Nota, Void> colAcciones;

    //============= INITIALIZATION =============
    @FXML
    public void initialize() {
        configurarVinculaciones();
        configurarColumnas();
        configurarAccionesTabla();
    }

    private void configurarVinculaciones() {
        pantallaBusqueda.visibleProperty().bind(modoBusqueda);
        pantallaNotas.visibleProperty().bind(modoBusqueda.not());
    }

    private void configurarColumnas() {
        colMateria.setCellValueFactory(new PropertyValueFactory<>("nombreMateria"));
        colNota.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    //============= TABLE ACTIONS =============
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
                    Nota nota = getTableView().getItems().get(getIndex());
                    editarNota(nota);
                });

                // Configurar botón Eliminar
                SVGPath iconoEliminar = new SVGPath();
                iconoEliminar.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                btnEliminar.setGraphic(iconoEliminar);
                btnEliminar.getStyleClass().addAll("action-button", "delete-button");
                btnEliminar.setOnAction(e -> {
                    Nota nota = getTableView().getItems().get(getIndex());
                    eliminarNota(nota);
                });
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

    //============= CORE LOGIC =============
    @FXML
    private void handleBuscarEstudiante() {
        String cedula = txtCedulaBusqueda.getText().trim();
        if (cedula.isEmpty()) return;

        estudianteActual = estudianteDAO.buscarPorCedula(cedula);
        if (estudianteActual == null) {
            mostrarErrorEstudianteNoEncontrado();
        } else {
            modoBusqueda.set(false);
            cargarNotasEstudiante();
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

    //============= DATABASE OPERATIONS =============
    private void cargarNotasEstudiante() {
        ObservableList<Nota> notas = notaDAO.obtenerNotasPorEstudiante(estudianteActual.getId());
        tablaNotas.setItems(notas);
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

    //============= UTILITIES =============
    private void mostrarErrorEstudianteNoEncontrado() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Estudiante no encontrado");
        alert.setContentText("La cédula ingresada no está registrada.");

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/estudiantes.fxml")));
            Stage stage = (Stage) contenidoDinamico.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    //============= GETTERS PARA VINCULACIÓN =============
    public BooleanProperty modoBusquedaProperty() {
        return modoBusqueda;
    }

    public boolean isModoBusqueda() {
        return modoBusqueda.get();
    }

    public void setModoBusqueda(boolean modoBusqueda) {
        this.modoBusqueda.set(modoBusqueda);
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
package controlestudios.controllers;

import controlestudios.utils.PDFConstanciaGenerator;
import controlestudios.utils.PDFGenerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controlestudios.models.*;
import controlestudios.database.*;
import javafx.collections.ObservableList;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
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

        // Opcional: Gestionar espacio ocupado
        pantallaBusqueda.managedProperty().bind(modoBusqueda);
        pantallaNotas.managedProperty().bind(modoBusqueda.not());
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
                // Ícono Editar
                FontIcon iconoEditar = new FontIcon("mdi-pencil");
                iconoEditar.setIconSize(16);
                btnEditar.setGraphic(iconoEditar);
                btnEditar.getStyleClass().addAll("action-button", "edit-button");

                // Ícono Eliminar
                FontIcon iconoEliminar = new FontIcon("mdi-delete");
                iconoEliminar.setIconSize(16);
                btnEliminar.setGraphic(iconoEliminar);
                btnEliminar.getStyleClass().addAll("action-button", "delete-button");

                // Eventos
                btnEditar.setOnAction(e -> {
                    Nota nota = getTableView().getItems().get(getIndex());
                    editarNota(nota);
                });

                btnEliminar.setOnAction(e -> {
                    Nota nota = getTableView().getItems().get(getIndex());
                    eliminarNota(nota);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnEditar, btnEliminar));
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
            controller.setDialogStage(stage);
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
    @FXML
    private void handleDescargarBoleta() {
        if (estudianteActual == null) return;

        ObservableList<Nota> notas = notaDAO.obtenerNotasPorEstudiante(estudianteActual.getId());
        String rutaLogo = "src/main/resources/images/logo.png"; // Ajusta según tu estructura

        PDFGenerator.generarBoletaNotas(estudianteActual, notas, rutaLogo);

        // Mostrar mensaje de éxito
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Boleta generada");
        alert.setHeaderText("Descarga exitosa");
        alert.setContentText("El PDF se guardó en el escritorio.");
        alert.showAndWait();
    }

    @FXML
    private void handleGenerarConstancia() {
        if (estudianteActual != null) {
            PDFConstanciaGenerator.generarConstancia(estudianteActual, "/images/logo.png");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Constancia generada");
            alert.setHeaderText("Descarga exitosa");
            alert.setContentText("El PDF se guardó en el escritorio.");
            alert.showAndWait();
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleccione un estudiante primero").show();
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
            controller.setDialogStage(stage);
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
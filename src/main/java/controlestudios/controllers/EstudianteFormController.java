package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import controlestudios.models.Estudiante;
import java.time.LocalDate;

public class EstudianteFormController {

    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtCedula;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtSeccion;
    @FXML private ComboBox<Integer> cmbGrado; // Nuevo ComboBox para grado
    @FXML private Button btnGuardar;

    private Stage dialogStage;
    private Estudiante estudiante;
    private boolean guardado = false;

    @FXML
    private void initialize() {
        // Configurar opciones de grado (1 a 5)
        cmbGrado.getItems().addAll(1, 2, 3, 4, 5);
        cmbGrado.getSelectionModel().selectFirst();

        // Validación en tiempo real
        txtNombreCompleto.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtCedula.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtSeccion.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        dpFechaNacimiento.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cmbGrado.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos()); // Nueva validación

        validarCampos();
    }

    private void validarCampos() {
        boolean valido = !txtNombreCompleto.getText().trim().isEmpty()
                && !txtCedula.getText().trim().isEmpty()
                && !txtSeccion.getText().trim().isEmpty()
                && dpFechaNacimiento.getValue() != null
                && cmbGrado.getValue() != null; // Nueva condición

        btnGuardar.setDisable(!valido);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        if (estudiante != null) {
            txtNombreCompleto.setText(estudiante.getNombreCompleto());
            txtCedula.setText(estudiante.getCedula());
            dpFechaNacimiento.setValue(estudiante.getFechaNacimiento());
            txtSeccion.setText(estudiante.getSeccion());
            cmbGrado.setValue(estudiante.getGrado()); // Nuevo campo
        } else {
            this.estudiante = new Estudiante();
        }
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    private void handleGuardar() {
        estudiante.setNombreCompleto(txtNombreCompleto.getText().trim());
        estudiante.setCedula(txtCedula.getText().trim());
        estudiante.setFechaNacimiento(dpFechaNacimiento.getValue());
        estudiante.setSeccion(txtSeccion.getText().trim());
        estudiante.setGrado(cmbGrado.getValue()); // Nuevo campo
        guardado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }
}
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
    @FXML private Button btnGuardar;

    private Stage dialogStage;
    private Estudiante estudiante;
    private boolean guardado = false;

    @FXML
    private void initialize() {
        // Validación en tiempo real
        txtNombreCompleto.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtCedula.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtSeccion.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        dpFechaNacimiento.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        validarCampos();
        if (btnGuardar != null) {
            validarCampos();
        }
    }

    private void validarCampos() {
        boolean valido = !txtNombreCompleto.getText().trim().isEmpty()
                && !txtCedula.getText().trim().isEmpty()
                && !txtSeccion.getText().trim().isEmpty()
                && dpFechaNacimiento.getValue() != null;

        btnGuardar.setDisable(!valido);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        if (estudiante != null) {
            // Modo edición
            txtNombreCompleto.setText(estudiante.getNombreCompleto());
            txtCedula.setText(estudiante.getCedula());
            dpFechaNacimiento.setValue(estudiante.getFechaNacimiento());
            txtSeccion.setText(estudiante.getSeccion());
        } else {
            // Modo creación
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
        guardado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }
}
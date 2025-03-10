package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import controlestudios.models.Materia;
import javafx.stage.Stage;

public class MateriaFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtProfesor;
    @FXML private TextArea txtDescripcion;
    @FXML private Button saveButton;

    private Stage dialogStage;
    private Materia materia;
    private boolean guardado = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
        if (materia != null) {
            txtNombre.setText(materia.getNombre());
            txtProfesor.setText(materia.getProfesor());
            txtDescripcion.setText(materia.getDescripcion());
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    private void initialize() {
        // Validación en tiempo real
        txtNombre.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtProfesor.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        validarCampos();
    }

    private void validarCampos() {
        boolean valido = !txtNombre.getText().trim().isEmpty()
                && !txtProfesor.getText().trim().isEmpty();
        saveButton.setDisable(!valido);
    }

    @FXML
    private void handleGuardar() {
        if (materia == null) {
            materia = new Materia();
        }
        materia.setNombre(txtNombre.getText().trim());
        materia.setProfesor(txtProfesor.getText().trim());
        materia.setDescripcion(txtDescripcion.getText().trim());
        guardado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    public Materia getMateria() {
        return this.materia;
    }
}
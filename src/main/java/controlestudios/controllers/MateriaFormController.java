package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import controlestudios.models.Materia;

public class MateriaFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtProfesor;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<Integer> cmbGrado; // Nuevo ComboBox para grado
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
            cmbGrado.setValue(materia.getGrado()); // Nuevo campo
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    private void initialize() {
        // Configurar opciones de grado (1 a 5)
        cmbGrado.getItems().addAll(1, 2, 3, 4, 5);
        cmbGrado.getSelectionModel().selectFirst();

        // Validación en tiempo real
        txtNombre.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtProfesor.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cmbGrado.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos()); // Nueva validación
        validarCampos();
    }

    private void validarCampos() {
        boolean valido = !txtNombre.getText().trim().isEmpty()
                && !txtProfesor.getText().trim().isEmpty()
                && cmbGrado.getValue() != null; // Nueva condición

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
        materia.setGrado(cmbGrado.getValue()); // Nuevo campo
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
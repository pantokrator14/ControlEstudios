package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import controlestudios.models.*;
import controlestudios.database.*;
import javafx.collections.ObservableList;

public class NotaFormController {

    @FXML private ComboBox<Materia> cbMaterias;
    @FXML private TextField txtNota;
    @FXML private Button btnGuardar;

    private Stage dialogStage;
    private Nota nota;
    private boolean guardado = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMaterias(ObservableList<Materia> materias) {
        cbMaterias.setItems(materias);
    }

    public void setEstudiante(Estudiante estudiante) {
        this.nota = new Nota();
        this.nota.setIdEstudiante(estudiante.getId());
    }

    public void setNota(Nota nota) {
        this.nota = nota;
        if (nota != null) {
            cbMaterias.getSelectionModel().select(new Materia(nota.getIdMateria(), "", "", ""));
            txtNota.setText(String.valueOf(nota.getValor()));
        }
    }

    @FXML
    private void initialize() {
        txtNota.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cbMaterias.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        validarCampos();
    }

    private void validarCampos() {
        boolean valido = cbMaterias.getValue() != null
                && txtNota.getText().matches("^[0-9]{1,2}(\\.[0-9]{1,2})?$")
                && Double.parseDouble(txtNota.getText()) <= 20.0;

        btnGuardar.setDisable(!valido);
    }

    @FXML
    private void handleGuardar() {
        if (nota == null) nota = new Nota();

        nota.setIdMateria(cbMaterias.getValue().getId());
        nota.setValor(Double.parseDouble(txtNota.getText()));
        guardado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    public Nota getNota() {
        return nota;
    }

    public boolean isGuardado() {
        return guardado;
    }
}
package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import controlestudios.models.Materia;
import controlestudios.models.Estudiante;
import controlestudios.models.Nota;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class NotaFormController {

    @FXML private ComboBox<Materia> cbMaterias;
    @FXML private TextField txtNota;
    @FXML private Label lblFechaInfo;
    @FXML private DatePicker dpFechaRegistro; // Cambiado de Label a DatePicker
    @FXML private Button btnGuardar;

    private Stage dialogStage;
    private Nota nota;
    private boolean guardado = false;
    private Estudiante estudiante;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setMaterias(ObservableList<Materia> materias) {
        cbMaterias.setItems(materias);
        cbMaterias.getSelectionModel().selectFirst();
    }

    public void setNota(Nota nota) {
        this.nota = nota;
        if (nota != null) {
            cbMaterias.getSelectionModel().select(nota.getIdMateria());
            txtNota.setText(String.valueOf(nota.getValor()));
            dpFechaRegistro.setValue(nota.getFechaRegistro());
        } else {
            dpFechaRegistro.setValue(LocalDate.now());
        }
    }

    @FXML
    private void initialize() {
        // Configurar validación de campos
        txtNota.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cbMaterias.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        dpFechaRegistro.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        validarCampos();
        cbMaterias.setCellFactory(param -> new ListCell<Materia>() {
            @Override
            protected void updateItem(Materia materia, boolean empty) {
                super.updateItem(materia, empty);
                setText(empty ? null : materia.getNombre() + " - " + materia.getProfesor());
            }
        });

        cbMaterias.setButtonCell(new ListCell<Materia>() {
            @Override
            protected void updateItem(Materia materia, boolean empty) {
                super.updateItem(materia, empty);
                setText(empty ? null : materia.getNombre() + " - " + materia.getProfesor());
            }
        });
    }

    private void validarCampos() {
        boolean valido = cbMaterias.getValue() != null
                && !txtNota.getText().isEmpty()
                && dpFechaRegistro.getValue() != null;

        try {
            Double.parseDouble(txtNota.getText());
        } catch (NumberFormatException e) {
            valido = false;
        }

        btnGuardar.setDisable(!valido);
    }

    @FXML
    private void handleGuardar() {
        if (nota == null) {
            nota = new Nota();
        }

        nota.setIdEstudiante(estudiante.getId());
        nota.setIdMateria(cbMaterias.getValue().getId());
        nota.setValor(Double.parseDouble(txtNota.getText()));
        nota.setFechaRegistro(dpFechaRegistro.getValue());

        guardado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Nota getNota() {
        return nota;
    }
}
package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import controlestudios.models.*;
import controlestudios.utils.PeriodoUtil;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class NotaFormController {

    @FXML private ComboBox<Materia> cbMaterias;
    @FXML private TextField txtNota;
    @FXML private Button btnGuardar;
    @FXML private Label lblFechaRegistro; // Nueva etiqueta

    private Stage dialogStage;
    private Nota nota;
    private boolean guardado = false;

    @FXML
    private void initialize() {
        configurarComboBoxMaterias();
        agregarListenersValidacion();
        validarCampos();
    }

    private void configurarComboBoxMaterias() {
        cbMaterias.setCellFactory(param -> new ListCell<Materia>() {
            @Override
            protected void updateItem(Materia materia, boolean empty) {
                super.updateItem(materia, empty);
                setText(empty || materia == null ? null : materia.getNombre());
            }
        });

        cbMaterias.setButtonCell(new ListCell<Materia>() {
            @Override
            protected void updateItem(Materia materia, boolean empty) {
                super.updateItem(materia, empty);
                setText(empty || materia == null ? null : materia.getNombre());
            }
        });
    }

    private void agregarListenersValidacion() {
        txtNota.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cbMaterias.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        validarCampos();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMaterias(ObservableList<Materia> materias) {
        cbMaterias.setItems(materias);
    }

    public void setEstudiante(Estudiante estudiante) {
        this.nota = new Nota();
        this.nota.setIdEstudiante(estudiante.getId());

        // Mostrar fecha actual
        lblFechaRegistro.setText("Fecha de registro: " + LocalDate.now());
    }

    public void setNota(Nota nota) {
        this.nota = nota;
        if (nota != null) {
            cbMaterias.getItems().stream()
                    .filter(m -> m.getId() == nota.getIdMateria())
                    .findFirst()
                    .ifPresent(m -> cbMaterias.getSelectionModel().select(m));
            txtNota.setText(String.valueOf(nota.getValor()));

            // Mostrar fecha existente
            lblFechaRegistro.setText("Fecha de registro: " + nota.getFechaRegistro());
        }
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

        // Establecer fecha y año automáticamente
        if (nota.getFechaRegistro() == null) {
            nota.setFechaRegistro(LocalDate.now());
            nota.setAnioEscolar(PeriodoUtil.obtenerAnioEscolar());
        }

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
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

    // ============= INITIALIZATION =============
    @FXML
    private void initialize() {
        configurarComboBoxMaterias(); // Configurar cómo se muestran las materias
        agregarListenersValidacion(); // Validación en tiempo real
    }

    private void configurarComboBoxMaterias() {
        // Configurar cómo se muestran las materias en la lista desplegable
        cbMaterias.setCellFactory(param -> new ListCell<Materia>() {
            @Override
            protected void updateItem(Materia materia, boolean empty) {
                super.updateItem(materia, empty);
                setText(empty || materia == null ? null : materia.getNombre());
            }
        });

        // Configurar cómo se muestra la materia seleccionada
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
        validarCampos(); // Validación inicial
    }

    // ============= MÉTODOS PÚBLICOS =============
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
            // Buscar la materia en la lista del ComboBox usando el ID
            cbMaterias.getItems().stream()
                    .filter(m -> m.getId() == nota.getIdMateria())
                    .findFirst()
                    .ifPresent(m -> cbMaterias.getSelectionModel().select(m)); // Seleccionar la materia existente
            txtNota.setText(String.valueOf(nota.getValor()));
        }
    }

    // ============= LÓGICA DE VALIDACIÓN =============
    private void validarCampos() {
        boolean valido = cbMaterias.getValue() != null
                && txtNota.getText().matches("^[0-9]{1,2}(\\.[0-9]{1,2})?$")
                && Double.parseDouble(txtNota.getText()) <= 20.0;

        btnGuardar.setDisable(!valido);
    }

    // ============= MANEJADORES DE EVENTOS =============
    @FXML
    private void handleGuardar() {
        if (nota == null) nota = new Nota();

        nota.setIdMateria(cbMaterias.getValue().getId());
        nota.setValor(Double.parseDouble(txtNota.getText()));
        guardado = true;
        dialogStage.close(); // Ya no hay NullPointerException porque dialogStage está inicializado
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    // ============= GETTERS =============
    public Nota getNota() {
        return nota;
    }

    public boolean isGuardado() {
        return guardado;
    }
}
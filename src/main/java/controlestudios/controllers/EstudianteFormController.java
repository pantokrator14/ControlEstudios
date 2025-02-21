package controlestudios.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import controlestudios.models.Estudiante;
import javafx.stage.Stage;
import java.time.LocalDate;

public class EstudianteFormController {
    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtCedula;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtSeccion;

    private Stage dialogStage;
    private Estudiante estudiante;
    private boolean guardado = false;

    // Métodos similares a MateriaFormController
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        if (estudiante != null) {
            txtNombreCompleto.setText(estudiante.getNombreCompleto());
            txtCedula.setText(estudiante.getCedula());
            dpFechaNacimiento.setValue(estudiante.getFechaNacimiento());
            txtSeccion.setText(estudiante.getSeccion());
        }
    }

    @FXML
    private void handleGuardar() {
        estudiante = new Estudiante();
        estudiante.setNombreCompleto(txtNombreCompleto.getText());
        estudiante.setCedula(txtCedula.getText());
        estudiante.setFechaNacimiento(dpFechaNacimiento.getValue());
        estudiante.setSeccion(txtSeccion.getText());
        guardado = true;
        dialogStage.close();
    }
}
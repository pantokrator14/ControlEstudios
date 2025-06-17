package controlestudios.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Nota {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty idEstudiante = new SimpleIntegerProperty();
    private final IntegerProperty idMateria = new SimpleIntegerProperty();
    private final DoubleProperty valor = new SimpleDoubleProperty();
    private final StringProperty nombreMateria = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> fechaRegistro = new SimpleObjectProperty<>();
    private final IntegerProperty anioEscolar = new SimpleIntegerProperty();

    // Getters/Setters para propiedades
    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public int getIdEstudiante() {
        return idEstudiante.get();
    }

    public void setIdEstudiante(int value) {
        idEstudiante.set(value);
    }

    public int getIdMateria() {
        return idMateria.get();
    }

    public void setIdMateria(int value) {
        idMateria.set(value);
    }

    public double getValor() {
        return valor.get();
    }

    public void setValor(double value) {
        valor.set(value);
    }

    public String getNombreMateria() {
        return nombreMateria.get();
    }

    public void setNombreMateria(String value) {
        nombreMateria.set(value);
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro.get();
    }

    public void setFechaRegistro(LocalDate value) {
        fechaRegistro.set(value);
    }

    public int getAnioEscolar() {
        return anioEscolar.get();
    }

    public void setAnioEscolar(int value) {
        anioEscolar.set(value);
    }
}
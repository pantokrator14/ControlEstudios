package controlestudios.models;

public class Nota {
    private int id;
    private int estudianteId;
    private int materiaId;
    private double calificacion;

    // Constructor
    public Nota(int estudianteId, int materiaId, double calificacion) {
        this.estudianteId = estudianteId;
        this.materiaId = materiaId;
        this.calificacion = calificacion;
    }

    // Getters y Setters
}

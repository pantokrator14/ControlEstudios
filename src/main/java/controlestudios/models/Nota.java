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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}

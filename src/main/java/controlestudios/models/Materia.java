package controlestudios.models;

public class Materia {
    private int id;
    private String nombre;
    private String descripcion;
    private String nombreProfesor;

    // Constructor
    public Materia(String nombre, String descripcion, String nombreProfesor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombreProfesor = nombreProfesor;
    }

    // Getters y Setters
}

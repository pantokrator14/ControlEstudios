package controlestudios.models;

public class Materia {
    private int id;
    private String nombre;
    private String descripcion;
    private String profesor;

    // constructor vacío
    public Materia() {}
    // Constructor
    public Materia(String nombre, String descripcion, String profesor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesor = profesor;
    }

    public Materia(int idMateria, String s, String s1, String s2) {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProfesor() {
        return this.profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}

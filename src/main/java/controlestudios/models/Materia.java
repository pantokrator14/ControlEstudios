package controlestudios.models;

public class Materia {
    private int id;
    private String nombre;
    private String descripcion;
    private String profesor;
    private int grado;

    // constructor vacío
    public Materia() {}
    // Constructor
    public Materia(String nombre, String descripcion, String profesor, int grado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesor = profesor;
        this.grado = grado;
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
    public int getGrado() { return grado; }
    public void setGrado(int grado) { this.grado = grado; }
}

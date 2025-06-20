package controlestudios.models;

import java.time.LocalDate;

public class Estudiante {
    private int id;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String cedula;
    private String seccion;
    private int grado;

    // constructor vacío
    public Estudiante() {}
    // Constructor
    public Estudiante(String nombreCompleto, LocalDate fechaNacimiento, String cedula, String seccion, int grado) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.seccion = seccion;
        this.grado = grado;
    }

    public Estudiante(int id, String nombreCompleto, String cedula, LocalDate fechaNacimiento, String seccion, int grado) {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getGrado() { return grado; }
    public void setGrado(int grado) { this.grado = grado; }
}

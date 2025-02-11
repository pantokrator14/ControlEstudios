package controlestudios.models;

import java.time.LocalDate;

public class Estudiante {
    private int id;
    private String nombreCompleto;
    private Date fechaNacimiento;
    private String cedula;
    private String seccion;

    // Constructor
    public Estudiante(String nombreCompleto, Date fechaNacimiento, String cedula, String seccion) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.seccion = seccion;
    }

    // Getters y Setters
}

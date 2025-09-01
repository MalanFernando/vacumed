package com.vacunacion.model;

import java.time.LocalDate;

/**
 * Modelo para la tabla ninos
 */
public class Nino {
    private int idNino;
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String numCedula;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String historiaClinica;
    private String tipoSangre;
    private String direccion;
    private boolean activo;

    // Objeto relacionado
    private Usuario usuario;

    // Constructores
    public Nino() {}

    public Nino(int idUsuario, String nombres, String apellidos, String numCedula,
                LocalDate fechaNacimiento, String sexo, String historiaClinica, String tipoSangre) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numCedula = numCedula;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.historiaClinica = historiaClinica;
        this.tipoSangre = tipoSangre;
        this.activo = true;
    }

    // Getters y Setters
    public int getIdNino() {
        return idNino;
    }

    public void setIdNino(int idNino) {
        this.idNino = idNino;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumCedula() {
        return numCedula;
    }

    public void setNumCedula(String numCedula) {
        this.numCedula = numCedula;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(String historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el nombre completo del niño
     * @return nombres + apellidos
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Calcula la edad en meses
     * @return edad en meses
     */
    public int getEdadEnMeses() {
        if (fechaNacimiento == null) return 0;
        LocalDate ahora = LocalDate.now();
        return (int) java.time.temporal.ChronoUnit.MONTHS.between(fechaNacimiento, ahora);
    }

    /**
     * Calcula la edad en años
     * @return edad en años
     */
    public int getEdadEnAnios() {
        if (fechaNacimiento == null) return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    @Override
    public String toString() {
        return "Nino{" +
                "idNino=" + idNino +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", numCedula='" + numCedula + '\'' +
                ", historiaClinica='" + historiaClinica + '\'' +
                ", activo=" + activo +
                '}';
    }
}

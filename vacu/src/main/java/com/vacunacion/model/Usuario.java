package com.vacunacion.model;

import java.time.LocalDateTime;

/**
 * Modelo para la tabla usuarios
 */
public class Usuario {
    private int idUsuario;
    private int idTipoUsuario;
    private Integer idCentro;
    private String nombres;
    private String apellidos;
    private String numCedula;
    private String email;
    private String contrasena;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private boolean activo;
    private int intentosLogin;
    private boolean bloqueado;
    private LocalDateTime fechaBloqueo;

    // Objetos relacionados
    private TipoUsuario tipoUsuario;
    private CentroSalud centroSalud;

    // Constructores
    public Usuario() {}

    public Usuario(String nombres, String apellidos, String numCedula, String email, String contrasena) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numCedula = numCedula;
        this.email = email;
        this.contrasena = contrasena;
        this.activo = true;
        this.intentosLogin = 0;
        this.bloqueado = false;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public Integer getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(Integer idCentro) {
        this.idCentro = idCentro;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIntentosLogin() {
        return intentosLogin;
    }

    public void setIntentosLogin(int intentosLogin) {
        this.intentosLogin = intentosLogin;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public LocalDateTime getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(LocalDateTime fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public CentroSalud getCentroSalud() {
        return centroSalud;
    }

    public void setCentroSalud(CentroSalud centroSalud) {
        this.centroSalud = centroSalud;
    }

    /**
     * Obtiene el nombre completo del usuario
     * @return nombres + apellidos
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Verifica si el usuario es administrador
     * @return true si es admin
     */
    public boolean isAdmin() {
        return tipoUsuario != null && "ADMIN".equals(tipoUsuario.getNombre());
    }

    /**
     * Verifica si el usuario es médico
     * @return true si es médico
     */
    public boolean isMedico() {
        return tipoUsuario != null && "MEDICO".equals(tipoUsuario.getNombre());
    }

    /**
     * Verifica si el usuario es paciente
     * @return true si es paciente
     */
    public boolean isPaciente() {
        return tipoUsuario != null && "PACIENTE".equals(tipoUsuario.getNombre());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", numCedula='" + numCedula + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                ", bloqueado=" + bloqueado +
                '}';
    }
}

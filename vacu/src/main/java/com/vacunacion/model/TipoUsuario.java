package com.vacunacion.model;

/**
 * Modelo para la tabla tipo_usuario
 */
public class TipoUsuario {
    private int idTipoUsuario;
    private String nombre;
    private boolean activo;

    // Constructores
    public TipoUsuario() {}

    public TipoUsuario(int idTipoUsuario, String nombre, boolean activo) {
        this.idTipoUsuario = idTipoUsuario;
        this.nombre = nombre;
        this.activo = activo;
    }

    // Getters y Setters
    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "TipoUsuario{" +
                "idTipoUsuario=" + idTipoUsuario +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}

package com.vacunacion.model;

import java.time.LocalDate;

/**
 * Modelo para la tabla vacunas
 */
public class Vacuna {
    private int idVacuna;
    private String nombre;
    private String descripcion;
    private String codigoInternacional;
    private int edadRecomendadaMeses;
    private int dosisRequeridas;
    private int stock;
    private LocalDate fechaExpiracion;
    private String lote;
    private boolean activo;

    // Constructores
    public Vacuna() {}

    public Vacuna(String nombre, String codigoInternacional, int dosisRequeridas) {
        this.nombre = nombre;
        this.codigoInternacional = codigoInternacional;
        this.dosisRequeridas = dosisRequeridas;
        this.activo = true;
        this.stock = 0;
    }

    // Getters y Setters
    public int getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(int idVacuna) {
        this.idVacuna = idVacuna;
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

    public String getCodigoInternacional() {
        return codigoInternacional;
    }

    public void setCodigoInternacional(String codigoInternacional) {
        this.codigoInternacional = codigoInternacional;
    }

    public int getEdadRecomendadaMeses() {
        return edadRecomendadaMeses;
    }

    public void setEdadRecomendadaMeses(int edadRecomendadaMeses) {
        this.edadRecomendadaMeses = edadRecomendadaMeses;
    }

    public int getDosisRequeridas() {
        return dosisRequeridas;
    }

    public void setDosisRequeridas(int dosisRequeridas) {
        this.dosisRequeridas = dosisRequeridas;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Verifica si la vacuna está vencida
     * @return true si está vencida
     */
    public boolean isVencida() {
        return fechaExpiracion != null && fechaExpiracion.isBefore(LocalDate.now());
    }

    /**
     * Verifica si hay stock disponible
     * @return true si hay stock
     */
    public boolean tieneStock() {
        return stock > 0;
    }

    @Override
    public String toString() {
        return "Vacuna{" +
                "idVacuna=" + idVacuna +
                ", nombre='" + nombre + '\'' +
                ", codigoInternacional='" + codigoInternacional + '\'' +
                ", stock=" + stock +
                ", activo=" + activo +
                '}';
    }
}

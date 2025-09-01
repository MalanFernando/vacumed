package com.vacunacion.model;

import java.time.LocalDateTime;

/**
 * Modelo para la tabla citas
 */
public class Cita {
    private int idCita;
    private int idNino;
    private int idCentro;
    private Integer idVacuna;
    private LocalDateTime fechaCita;
    private String motivo;
    private String estado;
    private String observaciones;
    private LocalDateTime creadoEn;

    // Objetos relacionados
    private Nino nino;
    private CentroSalud centroSalud;
    private Vacuna vacuna;

    // Constructores
    public Cita() {}

    public Cita(int idNino, int idCentro, LocalDateTime fechaCita, String motivo) {
        this.idNino = idNino;
        this.idCentro = idCentro;
        this.fechaCita = fechaCita;
        this.motivo = motivo;
        this.estado = "PENDIENTE";
        this.creadoEn = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdNino() {
        return idNino;
    }

    public void setIdNino(int idNino) {
        this.idNino = idNino;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public Integer getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(Integer idVacuna) {
        this.idVacuna = idVacuna;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Nino getNino() {
        return nino;
    }

    public void setNino(Nino nino) {
        this.nino = nino;
    }

    public CentroSalud getCentroSalud() {
        return centroSalud;
    }

    public void setCentroSalud(CentroSalud centroSalud) {
        this.centroSalud = centroSalud;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }

    /**
     * Verifica si la cita está pendiente
     * @return true si está pendiente
     */
    public boolean isPendiente() {
        return "PENDIENTE".equals(estado);
    }

    /**
     * Verifica si la cita fue atendida
     * @return true si fue atendida
     */
    public boolean isAtendida() {
        return "ATENDIDA".equals(estado);
    }

    /**
     * Verifica si la cita fue cancelada
     * @return true si fue cancelada
     */
    public boolean isCancelada() {
        return "CANCELADA".equals(estado);
    }

    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fechaCita=" + fechaCita +
                ", motivo='" + motivo + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

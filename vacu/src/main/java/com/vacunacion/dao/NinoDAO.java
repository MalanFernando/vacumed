package com.vacunacion.dao;

import com.vacunacion.model.Nino;
import com.vacunacion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con niños/pacientes
 */
public class NinoDAO {

    /**
     * Obtiene todos los niños
     * @return Lista de niños
     */
    public List<Nino> findAll() {
        List<Nino> ninos = new ArrayList<>();
        String sql = "SELECT * FROM ninos ORDER BY nombres, apellidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ninos.add(mapResultSetToNino(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener niños: " + e.getMessage());
        }
        return ninos;
    }

    /**
     * Busca un niño por ID
     * @param id ID del niño
     * @return Niño encontrado o null
     */
    public Nino findById(int id) {
        String sql = "SELECT * FROM ninos WHERE id_nino = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToNino(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar niño por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca niños por nombre o cédula
     * @param termino término de búsqueda
     * @return Lista de niños encontrados
     */
    public List<Nino> search(String termino) {
        List<Nino> ninos = new ArrayList<>();
        String sql = "SELECT * FROM ninos WHERE nombres LIKE ? OR apellidos LIKE ? OR num_cedula LIKE ? ORDER BY nombres, apellidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termino + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ninos.add(mapResultSetToNino(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar niños: " + e.getMessage());
        }
        return ninos;
    }

    /**
     * Mapea un ResultSet a un objeto Nino
     * @param rs ResultSet de la consulta
     * @return Nino mapeado
     * @throws SQLException si hay error en el mapeo
     */
    private Nino mapResultSetToNino(ResultSet rs) throws SQLException {
        Nino nino = new Nino();
        nino.setIdNino(rs.getInt("id_nino"));
        nino.setNombres(rs.getString("nombres"));
        nino.setApellidos(rs.getString("apellidos"));
        nino.setNumCedula(rs.getString("num_cedula"));

        Date fechaNacimiento = rs.getDate("fecha_nacimiento");
        if (fechaNacimiento != null) {
            nino.setFechaNacimiento(fechaNacimiento.toLocalDate());
        }

        nino.setSexo(rs.getString("genero"));
        nino.setTipoSangre(rs.getString("tipo_sangre"));
        nino.setHistoriaClinica(rs.getString("num_historia_clinica"));
        nino.setIdUsuario(rs.getInt("id_padre"));

        return nino;
    }
}

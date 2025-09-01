package com.vacunacion.dao;

import com.vacunacion.model.Cita;
import com.vacunacion.model.Nino;
import com.vacunacion.model.CentroSalud;
import com.vacunacion.model.Vacuna;
import com.vacunacion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con citas
 */
public class CitaDAO {

    /**
     * Obtiene todas las citas
     * @return Lista de citas
     */
    public List<Cita> findAll() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, n.nombres as nino_nombres, n.apellidos as nino_apellidos, " +
                "cs.nombre as centro_nombre, v.nombre as vacuna_nombre " +
                "FROM citas c " +
                "INNER JOIN ninos n ON c.id_nino = n.id_nino " +
                "INNER JOIN centro_de_salud cs ON c.id_centro = cs.id_centro " +
                "LEFT JOIN vacunas v ON c.id_vacuna = v.id_vacuna " +
                "ORDER BY c.fecha_cita DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                citas.add(mapResultSetToCita(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Busca citas por nombre de paciente o vacuna
     * @param termino término de búsqueda
     * @return Lista de citas encontradas
     */
    public List<Cita> search(String termino) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, n.nombres as nino_nombres, n.apellidos as nino_apellidos, " +
                "cs.nombre as centro_nombre, v.nombre as vacuna_nombre " +
                "FROM citas c " +
                "INNER JOIN ninos n ON c.id_nino = n.id_nino " +
                "INNER JOIN centro_de_salud cs ON c.id_centro = cs.id_centro " +
                "LEFT JOIN vacunas v ON c.id_vacuna = v.id_vacuna " +
                "WHERE n.nombres LIKE ? OR n.apellidos LIKE ? OR v.nombre LIKE ? " +
                "ORDER BY c.fecha_cita DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termino + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                citas.add(mapResultSetToCita(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar citas: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Guarda una nueva cita
     * @param cita cita a guardar
     * @return true si se guardó correctamente
     */
    public boolean save(Cita cita) {
        String sql = "INSERT INTO citas (id_nino, id_centro, id_vacuna, fecha_cita, motivo, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cita.getIdNino());
            stmt.setInt(2, cita.getIdCentro());
            stmt.setObject(3, cita.getIdVacuna());
            stmt.setTimestamp(4, Timestamp.valueOf(cita.getFechaCita()));
            stmt.setString(5, cita.getMotivo());
            stmt.setString(6, cita.getObservaciones());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cita.setIdCita(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar cita: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza una cita
     * @param cita cita a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean update(Cita cita) {
        String sql = "UPDATE citas SET fecha_cita = ?, motivo = ?, estado = ?, observaciones = ? WHERE id_cita = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(cita.getFechaCita()));
            stmt.setString(2, cita.getMotivo());
            stmt.setString(3, cita.getEstado());
            stmt.setString(4, cita.getObservaciones());
            stmt.setInt(5, cita.getIdCita());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina una cita
     * @param idCita ID de la cita a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean delete(int idCita) {
        String sql = "DELETE FROM citas WHERE id_cita = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCita);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca una cita por ID
     * @param id ID de la cita
     * @return Cita encontrada o null
     */
    public Cita findById(int id) {
        String sql = "SELECT c.*, n.nombres as nino_nombres, n.apellidos as nino_apellidos, " +
                "cs.nombre as centro_nombre, v.nombre as vacuna_nombre " +
                "FROM citas c " +
                "INNER JOIN ninos n ON c.id_nino = n.id_nino " +
                "INNER JOIN centro_de_salud cs ON c.id_centro = cs.id_centro " +
                "LEFT JOIN vacunas v ON c.id_vacuna = v.id_vacuna " +
                "WHERE c.id_cita = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCita(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cita por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Mapea un ResultSet a un objeto Cita
     * @param rs ResultSet de la consulta
     * @return Cita mapeada
     * @throws SQLException si hay error en el mapeo
     */
    private Cita mapResultSetToCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(rs.getInt("id_cita"));
        cita.setIdNino(rs.getInt("id_nino"));
        cita.setIdCentro(rs.getInt("id_centro"));
        cita.setIdVacuna(rs.getObject("id_vacuna", Integer.class));

        Timestamp fechaCita = rs.getTimestamp("fecha_cita");
        if (fechaCita != null) {
            cita.setFechaCita(fechaCita.toLocalDateTime());
        }

        cita.setMotivo(rs.getString("motivo"));
        cita.setEstado(rs.getString("estado"));
        cita.setObservaciones(rs.getString("observaciones"));

        Timestamp creadoEn = rs.getTimestamp("creado_en");
        if (creadoEn != null) {
            cita.setCreadoEn(creadoEn.toLocalDateTime());
        }

        // Mapear objetos relacionados
        Nino nino = new Nino();
        nino.setIdNino(rs.getInt("id_nino"));
        nino.setNombres(rs.getString("nino_nombres"));
        nino.setApellidos(rs.getString("nino_apellidos"));
        cita.setNino(nino);

        CentroSalud centro = new CentroSalud();
        centro.setIdCentro(rs.getInt("id_centro"));
        centro.setNombre(rs.getString("centro_nombre"));
        cita.setCentroSalud(centro);

        if (rs.getObject("id_vacuna") != null) {
            Vacuna vacuna = new Vacuna();
            vacuna.setIdVacuna(rs.getInt("id_vacuna"));
            vacuna.setNombre(rs.getString("vacuna_nombre"));
            cita.setVacuna(vacuna);
        }

        return cita;
    }
}

package com.vacunacion.dao;

import com.vacunacion.model.Vacuna;
import com.vacunacion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con vacunas
 */
public class VacunaDAO {

    /**
     * Obtiene todas las vacunas
     * @return Lista de vacunas
     */
    public List<Vacuna> findAll() {
        List<Vacuna> vacunas = new ArrayList<>();
        String sql = "SELECT * FROM vacunas ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vacunas.add(mapResultSetToVacuna(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener vacunas: " + e.getMessage());
        }
        return vacunas;
    }

    /**
     * Busca vacunas por nombre o código
     * @param termino término de búsqueda
     * @return Lista de vacunas encontradas
     */
    public List<Vacuna> search(String termino) {
        List<Vacuna> vacunas = new ArrayList<>();
        String sql = "SELECT * FROM vacunas WHERE nombre LIKE ? OR codigo_internacional LIKE ? ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termino + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vacunas.add(mapResultSetToVacuna(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar vacunas: " + e.getMessage());
        }
        return vacunas;
    }

    /**
     * Busca una vacuna por ID
     * @param id ID de la vacuna
     * @return Vacuna encontrada o null
     */
    public Vacuna findById(int id) {
        String sql = "SELECT * FROM vacunas WHERE id_vacuna = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVacuna(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar vacuna por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Guarda una nueva vacuna
     * @param vacuna vacuna a guardar
     * @return true si se guardó correctamente
     */
    public boolean save(Vacuna vacuna) {
        String sql = "INSERT INTO vacunas (nombre, descripcion, codigo_internacional, " +
                "edad_recomendada_meses, dosis_requeridas, stock, fecha_expiracion, lote) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vacuna.getNombre());
            stmt.setString(2, vacuna.getDescripcion());
            stmt.setString(3, vacuna.getCodigoInternacional());
            stmt.setInt(4, vacuna.getEdadRecomendadaMeses());
            stmt.setInt(5, vacuna.getDosisRequeridas());
            stmt.setInt(6, vacuna.getStock());
            stmt.setDate(7, vacuna.getFechaExpiracion() != null ? Date.valueOf(vacuna.getFechaExpiracion()) : null);
            stmt.setString(8, vacuna.getLote());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    vacuna.setIdVacuna(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar vacuna: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza una vacuna
     * @param vacuna vacuna a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean update(Vacuna vacuna) {
        String sql = "UPDATE vacunas SET nombre = ?, descripcion = ?, stock = ?, " +
                "fecha_expiracion = ?, lote = ?, activo = ? WHERE id_vacuna = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vacuna.getNombre());
            stmt.setString(2, vacuna.getDescripcion());
            stmt.setInt(3, vacuna.getStock());
            stmt.setDate(4, vacuna.getFechaExpiracion() != null ? Date.valueOf(vacuna.getFechaExpiracion()) : null);
            stmt.setString(5, vacuna.getLote());
            stmt.setBoolean(6, vacuna.isActivo());
            stmt.setInt(7, vacuna.getIdVacuna());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar vacuna: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Vacuna
     * @param rs ResultSet de la consulta
     * @return Vacuna mapeada
     * @throws SQLException si hay error en el mapeo
     */
    private Vacuna mapResultSetToVacuna(ResultSet rs) throws SQLException {
        Vacuna vacuna = new Vacuna();
        vacuna.setIdVacuna(rs.getInt("id_vacuna"));
        vacuna.setNombre(rs.getString("nombre"));
        vacuna.setDescripcion(rs.getString("descripcion"));
        vacuna.setCodigoInternacional(rs.getString("codigo_internacional"));
        vacuna.setEdadRecomendadaMeses(rs.getInt("edad_recomendada_meses"));
        vacuna.setDosisRequeridas(rs.getInt("dosis_requeridas"));
        vacuna.setStock(rs.getInt("stock"));

        Date fechaExpiracion = rs.getDate("fecha_expiracion");
        if (fechaExpiracion != null) {
            vacuna.setFechaExpiracion(fechaExpiracion.toLocalDate());
        }

        vacuna.setLote(rs.getString("lote"));
        vacuna.setActivo(rs.getBoolean("activo"));
        return vacuna;
    }
}

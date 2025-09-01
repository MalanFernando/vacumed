package com.vacunacion.dao;

import com.vacunacion.model.CentroSalud;
import com.vacunacion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con centros de salud
 */
public class CentroSaludDAO {

    /**
     * Obtiene todos los centros de salud
     * @return Lista de centros de salud
     */
    public List<CentroSalud> findAll() {
        List<CentroSalud> centros = new ArrayList<>();
        String sql = "SELECT * FROM centro_de_salud ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                centros.add(mapResultSetToCentroSalud(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener centros de salud: " + e.getMessage());
        }
        return centros;
    }

    /**
     * Busca centros de salud por nombre o dirección
     * @param termino término de búsqueda
     * @return Lista de centros encontrados
     */
    public List<CentroSalud> search(String termino) {
        List<CentroSalud> centros = new ArrayList<>();
        String sql = "SELECT * FROM centro_de_salud WHERE nombre LIKE ? OR direccion LIKE ? ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termino + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                centros.add(mapResultSetToCentroSalud(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar centros de salud: " + e.getMessage());
        }
        return centros;
    }

    /**
     * Busca un centro por ID
     * @param id ID del centro
     * @return Centro encontrado o null
     */
    public CentroSalud findById(int id) {
        String sql = "SELECT * FROM centro_de_salud WHERE id_centro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCentroSalud(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar centro por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Guarda un nuevo centro de salud
     * @param centro centro a guardar
     * @return true si se guardó correctamente
     */
    public boolean save(CentroSalud centro) {
        String sql = "INSERT INTO centro_de_salud (nombre, direccion, telefono, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, centro.getNombre());
            stmt.setString(2, centro.getDireccion());
            stmt.setString(3, centro.getTelefono());
            stmt.setString(4, centro.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    centro.setIdCentro(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar centro de salud: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza un centro de salud
     * @param centro centro a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean update(CentroSalud centro) {
        String sql = "UPDATE centro_de_salud SET nombre = ?, direccion = ?, telefono = ?, email = ?, activo = ? WHERE id_centro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, centro.getNombre());
            stmt.setString(2, centro.getDireccion());
            stmt.setString(3, centro.getTelefono());
            stmt.setString(4, centro.getEmail());
            stmt.setBoolean(5, centro.isActivo());
            stmt.setInt(6, centro.getIdCentro());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar centro de salud: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto CentroSalud
     * @param rs ResultSet de la consulta
     * @return CentroSalud mapeado
     * @throws SQLException si hay error en el mapeo
     */
    private CentroSalud mapResultSetToCentroSalud(ResultSet rs) throws SQLException {
        CentroSalud centro = new CentroSalud();
        centro.setIdCentro(rs.getInt("id_centro"));
        centro.setNombre(rs.getString("nombre"));
        centro.setDireccion(rs.getString("direccion"));
        centro.setTelefono(rs.getString("telefono"));
        centro.setEmail(rs.getString("email"));
        centro.setActivo(rs.getBoolean("activo"));
        return centro;
    }
}

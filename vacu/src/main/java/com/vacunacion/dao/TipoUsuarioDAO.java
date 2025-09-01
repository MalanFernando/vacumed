package com.vacunacion.dao;

import com.vacunacion.model.TipoUsuario;
import com.vacunacion.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con tipos de usuario
 */
public class TipoUsuarioDAO {

    /**
     * Obtiene todos los tipos de usuario activos
     * @return Lista de tipos de usuario
     */
    public List<TipoUsuario> findAll() {
        List<TipoUsuario> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipo_usuario WHERE activo = true ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tipos.add(mapResultSetToTipoUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tipos de usuario: " + e.getMessage());
        }
        return tipos;
    }

    /**
     * Busca un tipo de usuario por ID
     * @param id ID del tipo de usuario
     * @return TipoUsuario encontrado o null
     */
    public TipoUsuario findById(int id) {
        String sql = "SELECT * FROM tipo_usuario WHERE id_tipo_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTipoUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar tipo de usuario por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Mapea un ResultSet a un objeto TipoUsuario
     * @param rs ResultSet de la consulta
     * @return TipoUsuario mapeado
     * @throws SQLException si hay error en el mapeo
     */
    private TipoUsuario mapResultSetToTipoUsuario(ResultSet rs) throws SQLException {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
        tipo.setNombre(rs.getString("nombre"));
        tipo.setActivo(rs.getBoolean("activo"));
        return tipo;
    }
}

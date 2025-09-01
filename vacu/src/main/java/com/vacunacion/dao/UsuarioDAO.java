package com.vacunacion.dao;

import com.vacunacion.model.Usuario;
import com.vacunacion.model.TipoUsuario;
import com.vacunacion.model.CentroSalud;
import com.vacunacion.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con usuarios
 */
public class UsuarioDAO {

    /**
     * Busca un usuario por cédula
     * @param cedula número de cédula
     * @return Usuario encontrado o null
     */
    public Usuario findByCedula(String cedula) {
        System.out.println("[DEBUG] Buscando usuario con cédula: " + cedula);

        String sql = "SELECT u.*, tu.nombre as tipo_nombre, cs.nombre as centro_nombre, " +
                "cs.direccion as centro_direccion, cs.telefono as centro_telefono, " +
                "cs.email as centro_email " +
                "FROM usuarios u " +
                "INNER JOIN tipo_usuario tu ON u.id_tipo_usuario = tu.id_tipo_usuario " +
                "LEFT JOIN centro_de_salud cs ON u.id_centro = cs.id_centro " +
                "WHERE u.num_cedula = ?";

        System.out.println("[DEBUG] SQL Query: " + sql);

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("[DEBUG] Conexión a base de datos obtenida: " + (conn != null ? "OK" : "FAILED"));

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, cedula);
                System.out.println("[DEBUG] Parámetro cédula establecido: " + cedula);

                ResultSet rs = stmt.executeQuery();
                System.out.println("[DEBUG] Query ejecutada");

                if (rs.next()) {
                    System.out.println("[DEBUG] Usuario encontrado en la base de datos");
                    Usuario usuario = mapResultSetToUsuario(rs);
                    System.out.println("[DEBUG] Usuario mapeado - ID: " + usuario.getIdUsuario() +
                            ", Nombres: " + usuario.getNombres() +
                            ", Tipo: " + usuario.getTipoUsuario().getNombre());
                    return usuario;
                } else {
                    System.out.println("[DEBUG] No se encontró usuario con cédula: " + cedula);

                    String countSql = "SELECT COUNT(*) as total FROM usuarios";
                    try (PreparedStatement countStmt = conn.prepareStatement(countSql);
                         ResultSet countRs = countStmt.executeQuery()) {
                        if (countRs.next()) {
                            int totalUsers = countRs.getInt("total");
                            System.out.println("[DEBUG] Total de usuarios en la base de datos: " + totalUsers);
                        }
                    }

                    String allCedulasSql = "SELECT num_cedula FROM usuarios LIMIT 10";
                    try (PreparedStatement cedulasStmt = conn.prepareStatement(allCedulasSql);
                         ResultSet cedulasRs = cedulasStmt.executeQuery()) {
                        System.out.println("[DEBUG] Cédulas existentes en la base de datos:");
                        while (cedulasRs.next()) {
                            System.out.println("[DEBUG] - " + cedulasRs.getString("num_cedula"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar usuario por cédula: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Busca un usuario por email
     * @param email correo electrónico
     * @return Usuario encontrado o null
     */
    public Usuario findByEmail(String email) {
        String sql = "SELECT u.*, tu.nombre as tipo_nombre, cs.nombre as centro_nombre, " +
                "cs.direccion as centro_direccion, cs.telefono as centro_telefono, " +
                "cs.email as centro_email " +
                "FROM usuarios u " +
                "INNER JOIN tipo_usuario tu ON u.id_tipo_usuario = tu.id_tipo_usuario " +
                "LEFT JOIN centro_de_salud cs ON u.id_centro = cs.id_centro " +
                "WHERE u.email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     */
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, tu.nombre as tipo_nombre, cs.nombre as centro_nombre, " +
                "cs.direccion as centro_direccion, cs.telefono as centro_telefono, " +
                "cs.email as centro_email " +
                "FROM usuarios u " +
                "INNER JOIN tipo_usuario tu ON u.id_tipo_usuario = tu.id_tipo_usuario " +
                "LEFT JOIN centro_de_salud cs ON u.id_centro = cs.id_centro " +
                "ORDER BY u.nombres, u.apellidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Busca usuarios por nombre o cédula
     * @param termino término de búsqueda
     * @return Lista de usuarios encontrados
     */
    public List<Usuario> search(String termino) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, tu.nombre as tipo_nombre, cs.nombre as centro_nombre, " +
                "cs.direccion as centro_direccion, cs.telefono as centro_telefono, " +
                "cs.email as centro_email " +
                "FROM usuarios u " +
                "INNER JOIN tipo_usuario tu ON u.id_tipo_usuario = tu.id_tipo_usuario " +
                "LEFT JOIN centro_de_salud cs ON u.id_centro = cs.id_centro " +
                "WHERE u.nombres LIKE ? OR u.apellidos LIKE ? OR u.num_cedula LIKE ? " +
                "ORDER BY u.nombres, u.apellidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termino + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Guarda un nuevo usuario
     * @param usuario usuario a guardar
     * @return true si se guardó correctamente
     */
    public boolean save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id_tipo_usuario, id_centro, nombres, apellidos, " +
                "num_cedula, email, contrasena, telefono, direccion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, usuario.getIdTipoUsuario());
            stmt.setObject(2, usuario.getIdCentro());
            stmt.setString(3, usuario.getNombres());
            stmt.setString(4, usuario.getApellidos());
            stmt.setString(5, usuario.getNumCedula());
            stmt.setString(6, usuario.getEmail());
            stmt.setString(7, usuario.getContrasena());
            stmt.setString(8, usuario.getTelefono());
            stmt.setString(9, usuario.getDireccion());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setIdUsuario(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza un usuario existente
     * @param usuario usuario a actualizar
     * @return true si se actualizó correctamente
     */
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombres = ?, apellidos = ?, email = ?, " +
                "telefono = ?, direccion = ?, activo = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getTelefono());
            stmt.setString(5, usuario.getDireccion());
            stmt.setBoolean(6, usuario.isActivo());
            stmt.setInt(7, usuario.getIdUsuario());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza la contraseña de un usuario
     * @param idUsuario ID del usuario
     * @param nuevaContrasena nueva contraseña encriptada
     * @return true si se actualizó correctamente
     */
    public boolean updatePassword(int idUsuario, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevaContrasena);
            stmt.setInt(2, idUsuario);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza los intentos de login y estado de bloqueo
     * @param idUsuario ID del usuario
     * @param intentos número de intentos
     * @param bloqueado estado de bloqueo
     * @return true si se actualizó correctamente
     */
    public boolean updateLoginAttempts(int idUsuario, int intentos, boolean bloqueado) {
        String checkColumnsSql = "SHOW COLUMNS FROM usuarios LIKE 'intentos_login'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkColumnsSql);
             ResultSet rs = checkStmt.executeQuery()) {

            if (!rs.next()) {
                // Columns don't exist, skip the update
                System.out.println("[DEBUG] Login tracking columns don't exist, skipping update");
                return true;
            }

            // Columns exist, proceed with update
            String sql = "UPDATE usuarios SET intentos_login = ?, bloqueado = ?, fecha_bloqueo = ? WHERE id_usuario = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, intentos);
                stmt.setBoolean(2, bloqueado);
                stmt.setTimestamp(3, bloqueado ? Timestamp.valueOf(LocalDateTime.now()) : null);
                stmt.setInt(4, idUsuario);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar intentos de login: " + e.getMessage());
            // Return true to avoid blocking login flow
            return true;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Usuario
     * @param rs ResultSet de la consulta
     * @return Usuario mapeado
     * @throws SQLException si hay error en el mapeo
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
        usuario.setIdCentro(rs.getObject("id_centro", Integer.class));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setNumCedula(rs.getString("num_cedula"));
        usuario.setEmail(rs.getString("email"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setDireccion(rs.getString("direccion"));

        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            usuario.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }

        usuario.setActivo(rs.getBoolean("activo"));

        try {
            usuario.setIntentosLogin(rs.getInt("intentos_login"));
        } catch (SQLException e) {
            usuario.setIntentosLogin(0); // Default value if column doesn't exist
        }

        try {
            usuario.setBloqueado(rs.getBoolean("bloqueado"));
        } catch (SQLException e) {
            usuario.setBloqueado(false); // Default value if column doesn't exist
        }

        try {
            Timestamp fechaBloqueo = rs.getTimestamp("fecha_bloqueo");
            if (fechaBloqueo != null) {
                usuario.setFechaBloqueo(fechaBloqueo.toLocalDateTime());
            }
        } catch (SQLException e) {
            usuario.setFechaBloqueo(null); // Default value if column doesn't exist
        }

        // Mapear tipo de usuario
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
        tipoUsuario.setNombre(rs.getString("tipo_nombre"));
        usuario.setTipoUsuario(tipoUsuario);

        // Mapear centro de salud si existe
        if (rs.getObject("id_centro") != null) {
            CentroSalud centroSalud = new CentroSalud();
            centroSalud.setIdCentro(rs.getInt("id_centro"));
            centroSalud.setNombre(rs.getString("centro_nombre"));
            centroSalud.setDireccion(rs.getString("centro_direccion"));
            centroSalud.setTelefono(rs.getString("centro_telefono"));
            centroSalud.setEmail(rs.getString("centro_email"));
            usuario.setCentroSalud(centroSalud);
        }

        return usuario;
    }
}

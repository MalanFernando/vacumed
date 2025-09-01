package com.vacunacion.service;

import com.vacunacion.dao.UsuarioDAO;
import com.vacunacion.model.Usuario;
import com.vacunacion.util.PasswordUtils;
import com.vacunacion.util.ValidationUtils;

/**
 * Servicio para autenticación y autorización
 */
public class AuthService {

    private UsuarioDAO usuarioDAO;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autentica un usuario por cédula y contraseña
     * @param cedula número de cédula
     * @param password contraseña
     * @return usuario autenticado o null si falla
     */
    public Usuario authenticate(String cedula, String password) {
        System.out.println("[DEBUG] AuthService.authenticate - Cedula: " + cedula);
        System.out.println("[DEBUG] AuthService.authenticate - Password: " + password);

        // Validar formato de entrada
        if (!ValidationUtils.isNotEmpty(cedula) || !ValidationUtils.isNotEmpty(password)) {
            System.out.println("[DEBUG] Empty cedula or password");
            return null;
        }

        // Buscar usuario por cédula
        Usuario usuario = usuarioDAO.findByCedula(cedula);
        if (usuario == null) {
            System.out.println("[DEBUG] User not found for cedula: " + cedula);
            return null;
        }

        System.out.println("[DEBUG] User found: " + usuario.getNombres() + " " + usuario.getApellidos());
        System.out.println("[DEBUG] User ID: " + usuario.getIdUsuario());
        System.out.println("[DEBUG] User active: " + usuario.isActivo());
        System.out.println("[DEBUG] User blocked: " + usuario.isBloqueado());
        System.out.println("[DEBUG] Login attempts: " + usuario.getIntentosLogin());
        System.out.println("[DEBUG] User type: " + usuario.getTipoUsuario().getNombre());

        // Verificar si la cuenta está bloqueada
        if (usuario.isBloqueado()) {
            System.out.println("[DEBUG] Account is blocked");
            return null;
        }

        // Verificar si el usuario está activo
        if (!usuario.isActivo()) {
            System.out.println("[DEBUG] Account is inactive");
            return null;
        }

        System.out.println("[DEBUG] Stored password hash: " + usuario.getContrasena());
        System.out.println("[DEBUG] Provided password: " + password);

        // Verificar contraseña
        boolean passwordMatch = PasswordUtils.verifyPassword(password, usuario.getContrasena());
        System.out.println("[DEBUG] Password match: " + passwordMatch);

        if (passwordMatch) {
            // Login exitoso - resetear intentos
            if (usuario.getIntentosLogin() > 0) {
                usuarioDAO.updateLoginAttempts(usuario.getIdUsuario(), 0, false);
                usuario.setIntentosLogin(0);
            }
            System.out.println("[DEBUG] Authentication successful");
            return usuario;
        } else {
            // Login fallido - incrementar intentos
            int intentos = usuario.getIntentosLogin() + 1;
            boolean bloqueado = intentos >= MAX_LOGIN_ATTEMPTS;

            System.out.println("[DEBUG] Authentication failed. Attempts: " + intentos + ", Blocked: " + bloqueado);
            usuarioDAO.updateLoginAttempts(usuario.getIdUsuario(), intentos, bloqueado);
            return null;
        }
    }

    /**
     * Registra un nuevo usuario
     * @param usuario datos del usuario
     * @param password contraseña en texto plano
     * @param confirmPassword confirmación de contraseña
     * @return resultado del registro
     */
    public RegistrationResult registerUser(Usuario usuario, String password, String confirmPassword) {
        // Validar datos de entrada
        if (!ValidationUtils.isNotEmpty(usuario.getNombres())) {
            return new RegistrationResult(false, "El nombre es requerido");
        }

        if (!ValidationUtils.isNotEmpty(usuario.getApellidos())) {
            return new RegistrationResult(false, "Los apellidos son requeridos");
        }

        if (!ValidationUtils.isValidCedula(usuario.getNumCedula())) {
            return new RegistrationResult(false, "Número de cédula inválido");
        }

        if (!ValidationUtils.isValidEmail(usuario.getEmail())) {
            return new RegistrationResult(false, "Email inválido");
        }

        if (!ValidationUtils.isValidPassword(password)) {
            return new RegistrationResult(false, "La contraseña debe tener mínimo 6 caracteres con letras y números");
        }

        if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
            return new RegistrationResult(false, "Las contraseñas no coinciden");
        }

        // Verificar que la cédula no esté registrada
        if (usuarioDAO.findByCedula(usuario.getNumCedula()) != null) {
            return new RegistrationResult(false, "Ya existe un usuario con esta cédula");
        }

        // Verificar que el email no esté registrado
        if (usuarioDAO.findByEmail(usuario.getEmail()) != null) {
            return new RegistrationResult(false, "Ya existe un usuario con este email");
        }

        // Encriptar contraseña y guardar usuario
        usuario.setContrasena(PasswordUtils.hashPassword(password));
        usuario.setNumCedula(ValidationUtils.cleanCedula(usuario.getNumCedula()));
        usuario.setEmail(ValidationUtils.cleanEmail(usuario.getEmail()));

        if (usuarioDAO.save(usuario)) {
            return new RegistrationResult(true, "Usuario registrado exitosamente");
        } else {
            return new RegistrationResult(false, "Error al registrar usuario");
        }
    }

    /**
     * Cambia la contraseña de un usuario
     * @param idUsuario ID del usuario
     * @param newPassword nueva contraseña
     * @param confirmPassword confirmación de contraseña
     * @return true si se cambió exitosamente
     */
    public boolean changePassword(int idUsuario, String newPassword, String confirmPassword) {
        if (!ValidationUtils.isValidPassword(newPassword)) {
            return false;
        }

        if (!ValidationUtils.passwordsMatch(newPassword, confirmPassword)) {
            return false;
        }

        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        return usuarioDAO.updatePassword(idUsuario, hashedPassword);
    }

    /**
     * Desbloquea una cuenta de usuario
     * @param idUsuario ID del usuario
     * @return true si se desbloqueó exitosamente
     */
    public boolean unlockAccount(int idUsuario) {
        return usuarioDAO.updateLoginAttempts(idUsuario, 0, false);
    }

    /**
     * Clase para el resultado del registro
     */
    public static class RegistrationResult {
        private boolean success;
        private String message;

        public RegistrationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}

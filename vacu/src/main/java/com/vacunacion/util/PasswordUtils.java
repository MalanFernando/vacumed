package com.vacunacion.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidades para manejo de contraseñas
 */
public class PasswordUtils {

    // Número de rondas para el hash BCrypt
    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Encripta una contraseña usando BCrypt
     * @param plainPassword contraseña en texto plano
     * @return contraseña encriptada
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verifica si una contraseña coincide con su hash
     * @param plainPassword contraseña en texto plano
     * @param hashedPassword contraseña encriptada
     * @return true si coinciden
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Genera una contraseña temporal aleatoria
     * @return contraseña temporal
     */
    public static String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    /**
     * Método de prueba para verificar que el hash BCrypt funcione correctamente
     * Prueba específicamente con admin123 y el hash proporcionado
     */
    public static void testBCryptHash() {
        String password = "admin123";
        String expectedHash = "$2a$12$Rnp5ojb4HQ84uLIu5R3xAuvnmPyTawRHO5uTHtsR1ncEBxnHSIxSS";

        System.out.println("[BCrypt Test] Testing password: " + password);
        System.out.println("[BCrypt Test] Expected hash: " + expectedHash);

        boolean result = verifyPassword(password, expectedHash);
        System.out.println("[BCrypt Test] Verification result: " + result);

        // También generar un nuevo hash para comparar
        String newHash = hashPassword(password);
        System.out.println("[BCrypt Test] New generated hash: " + newHash);

        boolean newResult = verifyPassword(password, newHash);
        System.out.println("[BCrypt Test] New hash verification: " + newResult);
    }
}

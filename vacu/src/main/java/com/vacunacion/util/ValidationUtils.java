package com.vacunacion.util;

import java.util.regex.Pattern;

/**
 * Utilidades para validación de datos
 */
public class ValidationUtils {

    // Patrón para validar cédula (10 dígitos)
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^[0-9]{10}$");

    // Patrón para validar contraseña (mínimo 6 caracteres, letras y números)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$");

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Valida el formato de una cédula con validaciones muy simples
     * @param cedula número de cédula
     * @return true si es válida
     */
    public static boolean isValidCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        // Check if all characters are digits
        for (char c : cedula.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        // Check if not all zeros
        if (cedula.equals("0000000000")) {
            return false;
        }

        return true;
    }

    /**
     * Valida el formato de una contraseña
     * @param password contraseña a validar
     * @return true si es válida
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Valida el formato de un email
     * @param email email a validar
     * @return true si es válido
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida que un string no esté vacío
     * @param value valor a validar
     * @return true si no está vacío
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Valida que las contraseñas coincidan
     * @param password contraseña
     * @param confirmPassword confirmación de contraseña
     * @return true si coinciden
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * Limpia y formatea una cédula
     * @param cedula cédula a limpiar
     * @return cédula limpia
     */
    public static String cleanCedula(String cedula) {
        if (cedula == null) return null;
        return cedula.replaceAll("[^0-9]", "");
    }

    /**
     * Limpia y formatea un email
     * @param email email a limpiar
     * @return email limpio
     */
    public static String cleanEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }
}

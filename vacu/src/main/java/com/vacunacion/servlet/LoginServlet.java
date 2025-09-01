package com.vacunacion.servlet;

import com.vacunacion.model.Usuario;
import com.vacunacion.dao.UsuarioDAO;
import com.vacunacion.util.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para manejo de login
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cedula = request.getParameter("cedula");
        String password = request.getParameter("password");

        System.out.println("[LOGIN] Attempting login with cedula: " + cedula);

        if (cedula != null && password != null && cedula.length() == 10) {
            try {
                Usuario usuario = usuarioDAO.findByCedula(cedula);

                if (usuario != null) {
                    System.out.println("[LOGIN] User found: " + usuario.getNombres() + " " + usuario.getApellidos());
                    System.out.println("[LOGIN] User active: " + usuario.isActivo());
                    System.out.println("[LOGIN] User blocked: " + usuario.isBloqueado());

                    if (!usuario.isActivo()) {
                        System.out.println("[LOGIN] User is inactive");
                        request.setAttribute("error", "Usuario inactivo");
                        request.setAttribute("cedula", cedula);
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }

                    if (usuario.isBloqueado()) {
                        System.out.println("[LOGIN] User is blocked");
                        request.setAttribute("error", "Usuario bloqueado");
                        request.setAttribute("cedula", cedula);
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    }

                    System.out.println("[LOGIN] Verifying password...");
                    if (PasswordUtils.verifyPassword(password, usuario.getContrasena())) {
                        System.out.println("[LOGIN] Password verified successfully");

                        // Reset login attempts on successful login
                        usuarioDAO.updateLoginAttempts(usuario.getIdUsuario(), 0, false);

                        HttpSession session = request.getSession();
                        session.setAttribute("usuario", usuario);

                        // Determine user type based on id_tipo_usuario
                        String tipoUsuario = "PACIENTE"; // default
                        if (usuario.getIdTipoUsuario() == 1) {
                            tipoUsuario = "ADMIN";
                        } else if (usuario.getIdTipoUsuario() == 2) {
                            tipoUsuario = "MEDICO";
                        }

                        session.setAttribute("tipoUsuario", tipoUsuario);
                        session.setMaxInactiveInterval(30 * 60);

                        System.out.println("[LOGIN] Login successful, redirecting to dashboard");

                        // Redirect based on user type
                        if ("ADMIN".equals(tipoUsuario)) {
                            response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                        }
                        return;
                    } else {
                        System.out.println("[LOGIN] Password verification failed");

                        // Increment login attempts
                        int newAttempts = usuario.getIntentosLogin() + 1;
                        boolean shouldBlock = newAttempts >= 3;
                        usuarioDAO.updateLoginAttempts(usuario.getIdUsuario(), newAttempts, shouldBlock);

                        if (shouldBlock) {
                            request.setAttribute("error", "Usuario bloqueado por múltiples intentos fallidos");
                        } else {
                            request.setAttribute("error", "Credenciales incorrectas. Intentos restantes: " + (3 - newAttempts));
                        }
                    }
                } else {
                    System.out.println("[LOGIN] User not found for cedula: " + cedula);
                    request.setAttribute("error", "Usuario no encontrado");
                }
            } catch (Exception e) {
                System.out.println("[LOGIN] Database error: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Error de conexión a la base de datos");
            }
        } else {
            System.out.println("[LOGIN] Invalid input parameters");
            request.setAttribute("error", "Datos inválidos");
        }

        System.out.println("[LOGIN] Login failed, returning to login page");
        request.setAttribute("cedula", cedula);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}

package com.vacunacion.servlet;

import com.vacunacion.dao.UsuarioDAO;
import com.vacunacion.model.Usuario;
import com.vacunacion.service.AuthService;
import com.vacunacion.util.ValidationUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para actualizar perfil de usuario
 */
@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        authService = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener parámetros del formulario
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validar datos básicos
        if (!ValidationUtils.isNotEmpty(nombres) || !ValidationUtils.isNotEmpty(apellidos)) {
            request.setAttribute("error", "Nombres y apellidos son requeridos");
            redirectToDashboard(request, response, usuarioSesion);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            request.setAttribute("error", "Email inválido");
            redirectToDashboard(request, response, usuarioSesion);
            return;
        }

        // Actualizar datos del usuario
        usuarioSesion.setNombres(nombres);
        usuarioSesion.setApellidos(apellidos);
        usuarioSesion.setEmail(ValidationUtils.cleanEmail(email));
        usuarioSesion.setTelefono(telefono);
        usuarioSesion.setDireccion(direccion);

        // Cambiar contraseña si se proporcionó
        boolean passwordChanged = false;
        if (ValidationUtils.isNotEmpty(password)) {
            if (!ValidationUtils.isValidPassword(password)) {
                request.setAttribute("error", "La contraseña debe tener mínimo 6 caracteres con letras y números");
                redirectToDashboard(request, response, usuarioSesion);
                return;
            }

            if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                redirectToDashboard(request, response, usuarioSesion);
                return;
            }

            passwordChanged = authService.changePassword(usuarioSesion.getIdUsuario(), password, confirmPassword);
        }

        // Actualizar en la base de datos
        boolean updated = usuarioDAO.update(usuarioSesion);

        if (updated) {
            // Actualizar sesión
            session.setAttribute("usuario", usuarioSesion);
            request.setAttribute("success", "Perfil actualizado exitosamente" +
                    (passwordChanged ? " (contraseña cambiada)" : ""));
        } else {
            request.setAttribute("error", "Error al actualizar perfil");
        }

        redirectToDashboard(request, response, usuarioSesion);
    }

    /**
     * Redirige al dashboard correspondiente según el tipo de usuario
     */
    private void redirectToDashboard(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (usuario.isAdmin()) {
            response.sendRedirect("admin/dashboard");
        } else if (usuario.isMedico()) {
            response.sendRedirect("medico/dashboard.jsp");
        } else {
            response.sendRedirect("paciente/dashboard.jsp");
        }
    }
}

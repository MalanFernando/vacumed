package com.vacunacion.servlet;

import com.vacunacion.dao.TipoUsuarioDAO;
import com.vacunacion.model.Usuario;
import com.vacunacion.model.TipoUsuario;
import com.vacunacion.service.AuthService;
import com.vacunacion.util.ValidationUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para manejo de registro de usuarios
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private AuthService authService;
    private TipoUsuarioDAO tipoUsuarioDAO;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        tipoUsuarioDAO = new TipoUsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar tipos de usuario para el formulario
        List<TipoUsuario> tiposUsuario = tipoUsuarioDAO.findAll();
        request.setAttribute("tiposUsuario", tiposUsuario);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String cedula = request.getParameter("cedula");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String esPersonalMedico = request.getParameter("esPersonalMedico");

        // Crear objeto usuario
        Usuario usuario = new Usuario();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setNumCedula(cedula);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);

        // Determinar tipo de usuario
        if ("si".equals(esPersonalMedico)) {
            usuario.setIdTipoUsuario(2); // MEDICO
        } else {
            usuario.setIdTipoUsuario(3); // PACIENTE
        }

        // Intentar registrar
        AuthService.RegistrationResult result = authService.registerUser(usuario, password, confirmPassword);

        if (result.isSuccess()) {
            // Registro exitoso
            request.setAttribute("success", result.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // Error en el registro
            request.setAttribute("error", result.getMessage());
            request.setAttribute("usuario", usuario); // Mantener datos en el formulario

            // Recargar tipos de usuario
            List<TipoUsuario> tiposUsuario = tipoUsuarioDAO.findAll();
            request.setAttribute("tiposUsuario", tiposUsuario);

            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}

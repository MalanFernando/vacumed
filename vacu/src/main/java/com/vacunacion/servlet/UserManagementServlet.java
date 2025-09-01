package com.vacunacion.servlet;

import com.vacunacion.dao.UsuarioDAO;
import com.vacunacion.dao.TipoUsuarioDAO;
import com.vacunacion.model.Usuario;
import com.vacunacion.model.TipoUsuario;
import com.vacunacion.util.ValidationUtils;
import com.vacunacion.util.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/user-management")
public class UserManagementServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;
    private TipoUsuarioDAO tipoUsuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        tipoUsuarioDAO = new TipoUsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "search":
                    searchUsers(request, response);
                    break;
                case "filter":
                    filterUsers(request, response);
                    break;
                case "toggle-status":
                    toggleUserStatus(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {
                case "create":
                    createUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                default:
                    response.sendRedirect("dashboard?tab=usuarios");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Usuario> usuarios = usuarioDAO.findAll();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String searchName = request.getParameter("searchName");

        List<Usuario> usuarios;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            usuarios = usuarioDAO.search(searchTerm.trim());
        } else if (searchName != null && !searchName.trim().isEmpty()) {
            usuarios = usuarioDAO.search(searchName.trim());
        } else {
            usuarios = usuarioDAO.findAll();
        }

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("searchName", searchName);
        request.setAttribute("activeTab", "usuarios");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void filterUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String userType = request.getParameter("userType");

        List<Usuario> usuarios = usuarioDAO.findAll();

        if ("pacientes".equals(userType)) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getIdTipoUsuario() == 3)
                    .collect(java.util.stream.Collectors.toList());
        } else if ("medicos".equals(userType)) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getIdTipoUsuario() == 2)
                    .collect(java.util.stream.Collectors.toList());
        }

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("activeTab", "usuarios");
        request.setAttribute("selectedUserType", userType);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String cedula = request.getParameter("cedula");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String email = request.getParameter("email");
        String tipoUsuarioStr = request.getParameter("tipoUsuario");

        if (!ValidationUtils.isValidCedula(cedula)) {
            request.setAttribute("error", "Número de cédula inválido");
            request.setAttribute("activeTab", "usuarios");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        if (usuarioDAO.findByCedula(cedula) != null) {
            request.setAttribute("error", "Ya existe un usuario con esta cédula");
            request.setAttribute("activeTab", "usuarios");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setNumCedula(cedula);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setEmail(email);
        usuario.setIdTipoUsuario(Integer.parseInt(tipoUsuarioStr));

        String tempPassword = PasswordUtils.generateTemporaryPassword();
        usuario.setContrasena(PasswordUtils.hashPassword(tempPassword));
        usuario.setActivo(true);
        usuario.setBloqueado(false);
        usuario.setIntentosLogin(0);

        usuarioDAO.save(usuario);

        request.setAttribute("success", "Usuario creado exitosamente. Contraseña temporal: " + tempPassword);
        response.sendRedirect("dashboard?tab=usuarios");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String email = request.getParameter("email");

        List<Usuario> usuarios = usuarioDAO.findAll();
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getIdUsuario() == userId)
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setEmail(email);

            usuarioDAO.update(usuario);
            request.setAttribute("success", "Usuario actualizado exitosamente");
        }

        response.sendRedirect("dashboard?tab=usuarios");
    }

    private void toggleUserStatus(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String action = request.getParameter("statusAction");

        List<Usuario> usuarios = usuarioDAO.findAll();
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getIdUsuario() == userId)
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            switch (action) {
                case "activate":
                    usuario.setActivo(true);
                    usuario.setBloqueado(false);
                    usuarioDAO.updateLoginAttempts(userId, 0, false);
                    break;
                case "deactivate":
                    usuario.setActivo(false);
                    usuarioDAO.update(usuario);
                    break;
                case "unblock":
                    usuarioDAO.updateLoginAttempts(userId, 0, false);
                    break;
            }
        }

        response.sendRedirect("dashboard?tab=usuarios");
    }
}

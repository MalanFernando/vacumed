package com.vacunacion.servlet;

import com.vacunacion.dao.UsuarioDAO;
import com.vacunacion.dao.VacunaDAO;
import com.vacunacion.dao.CentroSaludDAO;
import com.vacunacion.dao.CitaDAO;
import com.vacunacion.model.Usuario;
import com.vacunacion.model.Vacuna;
import com.vacunacion.model.CentroSalud;
import com.vacunacion.model.Cita;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para el dashboard de administrador
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private VacunaDAO vacunaDAO;
    private CentroSaludDAO centroSaludDAO;
    private CitaDAO citaDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
        vacunaDAO = new VacunaDAO();
        centroSaludDAO = new CentroSaludDAO();
        citaDAO = new CitaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario currentUser = (Usuario) request.getSession().getAttribute("usuario");
        if (currentUser != null) {
            // Refresh user data from database to get latest info
            Usuario refreshedUser = usuarioDAO.findByCedula(currentUser.getNumCedula());
            if (refreshedUser != null) {
                request.setAttribute("currentUser", refreshedUser);
            } else {
                request.setAttribute("currentUser", currentUser);
            }
        }

        String action = request.getParameter("action");
        String tab = request.getParameter("tab");

        if (action != null) {
            handleAction(request, response, action);
            return;
        }

        // Cargar datos según la pestaña activa
        loadTabData(request, tab);

        // Redirigir al dashboard
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Maneja las acciones específicas del dashboard
     */
    private void handleAction(HttpServletRequest request, HttpServletResponse response, String action)
            throws ServletException, IOException {

        switch (action) {
            case "search":
                handleSearch(request, response);
                break;
            default:
                response.sendRedirect("dashboard");
                break;
        }
    }

    /**
     * Maneja las búsquedas
     */
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tab = request.getParameter("tab");
        String searchTerm = request.getParameter("searchTerm");

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadTabData(request, tab);
        } else {
            switch (tab) {
                case "usuarios":
                    List<Usuario> usuarios = usuarioDAO.search(searchTerm);
                    request.setAttribute("usuarios", usuarios);
                    break;
                case "vacunas":
                    List<Vacuna> vacunas = vacunaDAO.search(searchTerm);
                    request.setAttribute("vacunas", vacunas);
                    break;
                case "centros":
                    List<CentroSalud> centros = centroSaludDAO.search(searchTerm);
                    request.setAttribute("centros", centros);
                    break;
                case "citas":
                    List<Cita> citas = citaDAO.search(searchTerm);
                    request.setAttribute("citas", citas);
                    break;
            }
        }

        request.setAttribute("activeTab", tab);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    /**
     * Carga los datos según la pestaña activa
     */
    private void loadTabData(HttpServletRequest request, String tab) {
        if (tab == null) tab = "usuarios";

        switch (tab) {
            case "usuarios":
                List<Usuario> usuarios = usuarioDAO.findAll();
                request.setAttribute("usuarios", usuarios);
                break;
            case "vacunas":
                List<Vacuna> vacunas = vacunaDAO.findAll();
                request.setAttribute("vacunas", vacunas);
                break;
            case "centros":
                List<CentroSalud> centros = centroSaludDAO.findAll();
                request.setAttribute("centros", centros);
                break;
            case "citas":
                List<Cita> citas = citaDAO.findAll();
                request.setAttribute("citas", citas);
                break;
        }

        request.setAttribute("activeTab", tab);
    }
}

package com.vacunacion.servlet;

import com.vacunacion.dao.CentroSaludDAO;
import com.vacunacion.model.CentroSalud;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/medical-center-management")
public class MedicalCenterManagementServlet extends HttpServlet {
    private CentroSaludDAO centroSaludDAO;

    @Override
    public void init() throws ServletException {
        centroSaludDAO = new CentroSaludDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "search":
                    searchCenters(request, response);
                    break;
                case "toggle-status":
                    toggleCenterStatus(request, response);
                    break;
                default:
                    listCenters(request, response);
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
                    createCenter(request, response);
                    break;
                case "update":
                    updateCenter(request, response);
                    break;
                default:
                    response.sendRedirect("dashboard?tab=centros");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listCenters(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<CentroSalud> centros = centroSaludDAO.findAll();
        request.setAttribute("centros", centros);
        request.setAttribute("activeTab", "centros");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void searchCenters(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");

        List<CentroSalud> centros;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            centros = centroSaludDAO.search(searchTerm.trim());
        } else {
            centros = centroSaludDAO.findAll();
        }

        request.setAttribute("centros", centros);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("activeTab", "centros");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void createCenter(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        CentroSalud centro = new CentroSalud();
        centro.setNombre(nombre);
        centro.setDireccion(direccion);
        centro.setTelefono(telefono);
        centro.setEmail(email);
        centro.setActivo(true);

        if (centroSaludDAO.save(centro)) {
            request.setAttribute("success", "Centro médico creado exitosamente");
        } else {
            request.setAttribute("error", "Error al crear el centro médico");
        }

        response.sendRedirect("dashboard?tab=centros");
    }

    private void updateCenter(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int centerId = Integer.parseInt(request.getParameter("centerId"));
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        CentroSalud centro = centroSaludDAO.findById(centerId);
        if (centro != null) {
            centro.setNombre(nombre);
            centro.setDireccion(direccion);
            centro.setTelefono(telefono);
            centro.setEmail(email);

            if (centroSaludDAO.update(centro)) {
                request.setAttribute("success", "Centro médico actualizado exitosamente");
            } else {
                request.setAttribute("error", "Error al actualizar el centro médico");
            }
        }

        response.sendRedirect("dashboard?tab=centros");
    }

    private void toggleCenterStatus(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int centerId = Integer.parseInt(request.getParameter("centerId"));
        String statusAction = request.getParameter("statusAction");

        CentroSalud centro = centroSaludDAO.findById(centerId);
        if (centro != null) {
            switch (statusAction) {
                case "activate":
                    centro.setActivo(true);
                    break;
                case "deactivate":
                    centro.setActivo(false);
                    break;
            }
            centroSaludDAO.update(centro);
        }

        response.sendRedirect("dashboard?tab=centros");
    }
}

package com.vacunacion.servlet;

import com.vacunacion.dao.VacunaDAO;
import com.vacunacion.model.Vacuna;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/admin/vaccine-management")
public class VaccineManagementServlet extends HttpServlet {
    private VacunaDAO vacunaDAO;

    @Override
    public void init() throws ServletException {
        vacunaDAO = new VacunaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "search":
                    searchVaccines(request, response);
                    break;
                case "toggle-status":
                    toggleVaccineStatus(request, response);
                    break;
                default:
                    listVaccines(request, response);
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
                    createVaccine(request, response);
                    break;
                case "update":
                    updateVaccine(request, response);
                    break;
                default:
                    response.sendRedirect("dashboard?tab=vacunas");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listVaccines(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Vacuna> vacunas = vacunaDAO.findAll();
        request.setAttribute("vacunas", vacunas);
        request.setAttribute("activeTab", "vacunas");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void searchVaccines(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String searchCode = request.getParameter("searchCode");

        List<Vacuna> vacunas;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            vacunas = vacunaDAO.search(searchTerm.trim());
        } else if (searchCode != null && !searchCode.trim().isEmpty()) {
            vacunas = vacunaDAO.search(searchCode.trim());
        } else {
            vacunas = vacunaDAO.findAll();
        }

        request.setAttribute("vacunas", vacunas);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("searchCode", searchCode);
        request.setAttribute("activeTab", "vacunas");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void createVaccine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String codigoInternacional = request.getParameter("codigoInternacional");
        String edadRecomendadaStr = request.getParameter("edadRecomendada");
        String dosisRequeridasStr = request.getParameter("dosisRequeridas");
        String stockStr = request.getParameter("stock");
        String fechaExpiracionStr = request.getParameter("fechaExpiracion");
        String lote = request.getParameter("lote");

        try {
            Vacuna vacuna = new Vacuna();
            vacuna.setNombre(nombre);
            vacuna.setDescripcion(descripcion);
            vacuna.setCodigoInternacional(codigoInternacional);
            vacuna.setEdadRecomendadaMeses(Integer.parseInt(edadRecomendadaStr));
            vacuna.setDosisRequeridas(Integer.parseInt(dosisRequeridasStr));
            vacuna.setStock(Integer.parseInt(stockStr));

            if (fechaExpiracionStr != null && !fechaExpiracionStr.isEmpty()) {
                vacuna.setFechaExpiracion(LocalDate.parse(fechaExpiracionStr));
            }

            vacuna.setLote(lote);
            vacuna.setActivo(true);

            if (vacunaDAO.save(vacuna)) {
                request.setAttribute("success", "Vacuna creada exitosamente");
            } else {
                request.setAttribute("error", "Error al crear la vacuna");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("error", "Datos inválidos en el formulario");
        }

        response.sendRedirect("dashboard?tab=vacunas");
    }

    private void updateVaccine(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int vaccineId = Integer.parseInt(request.getParameter("vaccineId"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String stockStr = request.getParameter("stock");
        String fechaExpiracionStr = request.getParameter("fechaExpiracion");
        String lote = request.getParameter("lote");

        Vacuna vacuna = vacunaDAO.findById(vaccineId);
        if (vacuna != null) {
            try {
                vacuna.setNombre(nombre);
                vacuna.setDescripcion(descripcion);
                vacuna.setStock(Integer.parseInt(stockStr));

                if (fechaExpiracionStr != null && !fechaExpiracionStr.isEmpty()) {
                    vacuna.setFechaExpiracion(LocalDate.parse(fechaExpiracionStr));
                }

                vacuna.setLote(lote);

                if (vacunaDAO.update(vacuna)) {
                    request.setAttribute("success", "Vacuna actualizada exitosamente");
                } else {
                    request.setAttribute("error", "Error al actualizar la vacuna");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                request.setAttribute("error", "Datos inválidos en el formulario");
            }
        }

        response.sendRedirect("dashboard?tab=vacunas");
    }

    private void toggleVaccineStatus(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int vaccineId = Integer.parseInt(request.getParameter("vaccineId"));
        String statusAction = request.getParameter("statusAction");

        Vacuna vacuna = vacunaDAO.findById(vaccineId);
        if (vacuna != null) {
            switch (statusAction) {
                case "activate":
                    vacuna.setActivo(true);
                    break;
                case "deactivate":
                    vacuna.setActivo(false);
                    break;
            }
            vacunaDAO.update(vacuna);
        }

        response.sendRedirect("dashboard?tab=vacunas");
    }
}

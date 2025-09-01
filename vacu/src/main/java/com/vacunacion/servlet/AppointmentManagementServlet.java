package com.vacunacion.servlet;

import com.vacunacion.dao.CitaDAO;
import com.vacunacion.dao.NinoDAO;
import com.vacunacion.dao.CentroSaludDAO;
import com.vacunacion.dao.VacunaDAO;
import com.vacunacion.model.Cita;
import com.vacunacion.model.Nino;
import com.vacunacion.model.CentroSalud;
import com.vacunacion.model.Vacuna;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/admin/appointment-management")
public class AppointmentManagementServlet extends HttpServlet {
    private CitaDAO citaDAO;
    private NinoDAO ninoDAO;
    private CentroSaludDAO centroSaludDAO;
    private VacunaDAO vacunaDAO;

    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
        ninoDAO = new NinoDAO();
        centroSaludDAO = new CentroSaludDAO();
        vacunaDAO = new VacunaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "search":
                    searchAppointments(request, response);
                    break;
                case "delete":
                    deleteAppointment(request, response);
                    break;
                case "get-form-data":
                    getFormData(request, response);
                    break;
                default:
                    listAppointments(request, response);
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
                    createAppointment(request, response);
                    break;
                case "update":
                    updateAppointment(request, response);
                    break;
                default:
                    response.sendRedirect("dashboard?tab=citas");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listAppointments(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Cita> citas = citaDAO.findAll();
        request.setAttribute("citas", citas);
        request.setAttribute("activeTab", "citas");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void searchAppointments(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String searchVaccine = request.getParameter("searchVaccine");

        List<Cita> citas;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            citas = citaDAO.search(searchTerm.trim());
        } else if (searchVaccine != null && !searchVaccine.trim().isEmpty()) {
            citas = citaDAO.search(searchVaccine.trim());
        } else {
            citas = citaDAO.findAll();
        }

        request.setAttribute("citas", citas);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("searchVaccine", searchVaccine);
        request.setAttribute("activeTab", "citas");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void createAppointment(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String ninoIdStr = request.getParameter("ninoId");
        String centroIdStr = request.getParameter("centroId");
        String vacunaIdStr = request.getParameter("vacunaId");
        String fechaCitaStr = request.getParameter("fechaCita");
        String horaCitaStr = request.getParameter("horaCita");
        String motivo = request.getParameter("motivo");
        String observaciones = request.getParameter("observaciones");

        try {
            Cita cita = new Cita();
            cita.setIdNino(Integer.parseInt(ninoIdStr));
            cita.setIdCentro(Integer.parseInt(centroIdStr));

            if (vacunaIdStr != null && !vacunaIdStr.isEmpty()) {
                cita.setIdVacuna(Integer.parseInt(vacunaIdStr));
            }

            // Combinar fecha y hora
            String fechaHoraStr = fechaCitaStr + "T" + horaCitaStr;
            cita.setFechaCita(LocalDateTime.parse(fechaHoraStr));

            cita.setMotivo(motivo);
            cita.setObservaciones(observaciones);
            cita.setEstado("PROGRAMADA");

            if (citaDAO.save(cita)) {
                request.setAttribute("success", "Cita médica creada exitosamente");
            } else {
                request.setAttribute("error", "Error al crear la cita médica");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("error", "Datos inválidos en el formulario");
        }

        response.sendRedirect("dashboard?tab=citas");
    }

    private void updateAppointment(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int citaId = Integer.parseInt(request.getParameter("citaId"));
        String fechaCitaStr = request.getParameter("fechaCita");
        String horaCitaStr = request.getParameter("horaCita");
        String motivo = request.getParameter("motivo");
        String estado = request.getParameter("estado");
        String observaciones = request.getParameter("observaciones");

        Cita cita = citaDAO.findById(citaId);
        if (cita != null) {
            try {
                // Combinar fecha y hora
                String fechaHoraStr = fechaCitaStr + "T" + horaCitaStr;
                cita.setFechaCita(LocalDateTime.parse(fechaHoraStr));

                cita.setMotivo(motivo);
                cita.setEstado(estado);
                cita.setObservaciones(observaciones);

                if (citaDAO.update(cita)) {
                    request.setAttribute("success", "Cita médica actualizada exitosamente");
                } else {
                    request.setAttribute("error", "Error al actualizar la cita médica");
                }
            } catch (DateTimeParseException e) {
                request.setAttribute("error", "Fecha u hora inválida");
            }
        }

        response.sendRedirect("dashboard?tab=citas");
    }

    private void deleteAppointment(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int citaId = Integer.parseInt(request.getParameter("citaId"));

        if (citaDAO.delete(citaId)) {
            request.setAttribute("success", "Cita eliminada exitosamente");
        } else {
            request.setAttribute("error", "Error al eliminar la cita");
        }

        response.sendRedirect("dashboard?tab=citas");
    }

    private void getFormData(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Obtener datos para los formularios
        List<Nino> ninos = ninoDAO.findAll();
        List<CentroSalud> centros = centroSaludDAO.findAll();
        List<Vacuna> vacunas = vacunaDAO.findAll();

        request.setAttribute("ninos", ninos);
        request.setAttribute("centros", centros);
        request.setAttribute("vacunas", vacunas);
        request.setAttribute("activeTab", "citas");

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}

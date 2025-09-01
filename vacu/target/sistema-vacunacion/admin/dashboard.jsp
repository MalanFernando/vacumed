<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Administrador - VACU</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="../css/dashboard.css">
</head>
<body class="dashboard-container">
Header
<header class="dashboard-header">
    <nav class="dashboard-nav">
        <a href="../index.jsp" class="logo">VACU</a>
        <ul class="nav-links">
            <li><a href="#about">About</a></li>
            <li><a href="#learn">Learn</a></li>
            <li><a href="#maps">Maps</a></li>
        </ul>

        User Menu
        <div class="user-menu">
            <div class="user-avatar" onclick="toggleUserDropdown()">
                <span>${currentUser.nombres.charAt(0)}</span>
            </div>
            <div class="user-dropdown" id="userDropdown">
                <a href="#" onclick="openEditProfileModal()">Editar perfil</a>
                <a href="../LogoutServlet">Cerrar sesion</a>
            </div>
        </div>
    </nav>
</header>

Main Content
<main class="dashboard-main">
    Welcome Section
    <section class="welcome-section">
        <h1 class="welcome-title">Hola, Administrador</h1>
        <p class="welcome-subtitle">Bienvenido al sistema para registro de vacunas</p>

        <div class="user-info">
            <div class="user-avatar-large">
                <span>${currentUser.nombres.charAt(0)}</span>
            </div>
            <div class="user-details">
                Display real user data from database
                <h3>${currentUser.nombres} ${currentUser.apellidos}</h3>
                <p>C.I: ${currentUser.cedula}</p>
                <div style="display: flex; gap: 40px; margin-top: 10px;">
                    <div>
                        <strong>Telf:</strong>
                        <p>${currentUser.telefono != null ? currentUser.telefono : 'No registrado'}</p>
                    </div>
                    <div>
                        <strong>Correo:</strong>
                        <p>${currentUser.email != null ? currentUser.email : 'No registrado'}</p>
                    </div>
                    <div>
                        <strong>Direccion:</strong>
                        <p>${currentUser.direccion != null ? currentUser.direccion : 'No registrada'}</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    Tab Navigation
    <nav class="tab-navigation">
        <button class="tab-btn ${activeTab == 'usuarios' || activeTab == null ? 'active' : ''}" onclick="switchTab('usuarios')">Usuarios</button>
        <button class="tab-btn ${activeTab == 'vacunas' ? 'active' : ''}" onclick="switchTab('vacunas')">Vacunas</button>
        <button class="tab-btn ${activeTab == 'citas' ? 'active' : ''}" onclick="switchTab('citas')">Citas medicas</button>
        <button class="tab-btn ${activeTab == 'centros' ? 'active' : ''}" onclick="switchTab('centros')">Centros medicos</button>
    </nav>

    Content Sections

    Usuarios Tab
    <section class="content-section ${activeTab == 'usuarios' || activeTab == null ? 'active' : ''}" id="usuarios-section">
        <div class="section-header">
            <h2 class="section-title">Busqueda por:</h2>
        </div>

        <form class="search-form" action="dashboard" method="get">
            <input type="hidden" name="action" value="search">
            <input type="hidden" name="tab" value="usuarios">
            <input type="text" name="searchTerm" placeholder="Cedula o Nombre" class="search-input" value="${searchTerm}">
            <button type="submit" class="search-btn">Buscar</button>
        </form>

        <div class="section-header">
            <h3>Registros</h3>
            <div>
                <button class="tab-btn active" onclick="filterUsers('all')">Todos los usuarios</button>
                <button class="tab-btn" onclick="filterUsers('pacientes')">Pacientes</button>
                <button class="tab-btn" onclick="filterUsers('medicos')">Medicos</button>
                <button class="btn btn-primary" onclick="openCreateUserModal()">+ Crear usuario</button>
            </div>
        </div>

        Real user table with database data
        <table class="data-table">
            <thead>
            <tr>
                <th>Nro</th>
                <th>Nombre</th>
                <th>Cedula</th>
                <th>Tipo de usuario</th>
                <th>Status</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty usuarios}">
                    <c:forEach var="usuario" items="${usuarios}" varStatus="status">
                        <tr data-user-type="${usuario.tipoUsuario.nombre}">
                            <td>${status.index + 1}</td>
                            <td>${usuario.nombres} ${usuario.apellidos}</td>
                            <td>${usuario.cedula}</td>
                            <td>${usuario.tipoUsuario.nombre}</td>
                            <td>
                                        <span class="status-badge ${usuario.bloqueado ? 'status-inactive' : 'status-active'}">
                                                ${usuario.bloqueado ? 'Bloqueado' : 'Activo'}
                                        </span>
                            </td>
                            <td>
                                <div class="actions-dropdown">
                                    <button class="actions-btn" onclick="toggleActionsDropdown(this)">Acciones ▼</button>
                                    <div class="actions-menu">
                                        <a href="#" onclick="editUser(${usuario.idUsuario})">Editar</a>
                                        <c:if test="${usuario.bloqueado}">
                                            <a href="#" onclick="toggleUserStatus(${usuario.idUsuario}, false)">Activar</a>
                                        </c:if>
                                        <c:if test="${!usuario.bloqueado}">
                                            <a href="#" onclick="toggleUserStatus(${usuario.idUsuario}, true)">Desactivar</a>
                                        </c:if>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6" style="text-align: center; padding: 20px;">No se encontraron usuarios</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </section>

    Vacunas Tab
    <section class="content-section ${activeTab == 'vacunas' ? 'active' : ''}" id="vacunas-section">
        <div class="section-header">
            <h2 class="section-title">Busqueda por:</h2>
        </div>

        <form class="search-form" action="dashboard" method="get">
            <input type="hidden" name="action" value="search">
            <input type="hidden" name="tab" value="vacunas">
            <input type="text" name="searchTerm" placeholder="Nombre de vacuna" class="search-input" value="${searchTerm}">
            <button type="submit" class="search-btn">Buscar</button>
        </form>

        <div class="section-header">
            <h3>Registros</h3>
            <div>
                <button class="tab-btn active">Todas las vacunas</button>
                <button class="btn btn-primary" onclick="openCreateVaccineModal()">+ Crear vacuna</button>
            </div>
        </div>

        Real vaccine table with database data
        <table class="data-table">
            <thead>
            <tr>
                <th>Nro</th>
                <th>Nombre de Vacuna</th>
                <th>Codigo</th>
                <th>Fecha expiracion</th>
                <th>Stock</th>
                <th>Status</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty vacunas}">
                    <c:forEach var="vacuna" items="${vacunas}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td>${vacuna.nombre}</td>
                            <td>${vacuna.codigo}</td>
                            <td><fmt:formatDate value="${vacuna.fechaExpiracion}" pattern="dd/MM/yyyy"/></td>
                            <td>${vacuna.stock}</td>
                            <td>
                                        <span class="status-badge ${vacuna.stock > 0 ? 'status-active' : 'status-inactive'}">
                                                ${vacuna.stock > 0 ? 'Disponible' : 'Agotado'}
                                        </span>
                            </td>
                            <td>
                                <div class="actions-dropdown">
                                    <button class="actions-btn" onclick="toggleActionsDropdown(this)">Acciones ▼</button>
                                    <div class="actions-menu">
                                        <a href="#" onclick="editVaccine(${vacuna.idVacuna})">Editar</a>
                                        <a href="#" onclick="updateStock(${vacuna.idVacuna})">Actualizar Stock</a>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align: center; padding: 20px;">No se encontraron vacunas</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </section>

    Citas Tab
    <section class="content-section ${activeTab == 'citas' ? 'active' : ''}" id="citas-section">
        <div class="section-header">
            <h2 class="section-title">Busqueda por:</h2>
        </div>

        <form class="search-form" action="dashboard" method="get">
            <input type="hidden" name="action" value="search">
            <input type="hidden" name="tab" value="citas">
            <input type="text" name="searchTerm" placeholder="Nombre de paciente" class="search-input" value="${searchTerm}">
            <button type="submit" class="search-btn">Buscar</button>
        </form>

        <div class="section-header">
            <h3>Registros</h3>
            <div>
                <button class="tab-btn active">Todas las citas</button>
                <button class="btn btn-primary" onclick="openCreateAppointmentModal()">+ Crear cita medica</button>
            </div>
        </div>

        Real appointments table with database data
        <table class="data-table">
            <thead>
            <tr>
                <th>Nro</th>
                <th>Nombre del paciente</th>
                <th>Centro de salud</th>
                <th>Vacuna</th>
                <th>Fecha de cita</th>
                <th>Status</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty citas}">
                    <c:forEach var="cita" items="${citas}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td>${cita.nino.nombres} ${cita.nino.apellidos}</td>
                            <td>${cita.centroSalud.nombre}</td>
                            <td>${cita.vacuna.nombre}</td>
                            <td><fmt:formatDate value="${cita.fechaCita}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>
                                        <span class="status-badge ${cita.estado == 'COMPLETADA' ? 'status-active' : 'status-inactive'}">
                                                ${cita.estado}
                                        </span>
                            </td>
                            <td>
                                <div class="actions-dropdown">
                                    <button class="actions-btn" onclick="toggleActionsDropdown(this)">Acciones ▼</button>
                                    <div class="actions-menu">
                                        <a href="#" onclick="editAppointment(${cita.idCita})">Editar</a>
                                        <a href="#" onclick="cancelAppointment(${cita.idCita})">Cancelar</a>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align: center; padding: 20px;">No se encontraron citas</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </section>

    Centros Tab
    <section class="content-section ${activeTab == 'centros' ? 'active' : ''}" id="centros-section">
        <div class="section-header">
            <h2 class="section-title">Busqueda por:</h2>
        </div>

        <form class="search-form" action="dashboard" method="get">
            <input type="hidden" name="action" value="search">
            <input type="hidden" name="tab" value="centros">
            <input type="text" name="searchTerm" placeholder="Direccion del centro" class="search-input" value="${searchTerm}">
            <button type="submit" class="search-btn">Buscar</button>
        </form>

        <div class="section-header">
            <h3>Registros</h3>
            <div>
                <button class="tab-btn active">Todos los centros</button>
                <button class="btn btn-primary" onclick="openCreateCenterModal()">+ Crear centro medico</button>
            </div>
        </div>

        Real medical centers table with database data
        <table class="data-table">
            <thead>
            <tr>
                <th>Nro</th>
                <th>Nombre de Centro</th>
                <th>Direccion</th>
                <th>Telefono</th>
                <th>Email</th>
                <th>Status</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty centros}">
                    <c:forEach var="centro" items="${centros}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td>${centro.nombre}</td>
                            <td>${centro.direccion}</td>
                            <td>${centro.telefono}</td>
                            <td>${centro.email}</td>
                            <td>
                                        <span class="status-badge ${centro.activo ? 'status-active' : 'status-inactive'}">
                                                ${centro.activo ? 'Activo' : 'Inactivo'}
                                        </span>
                            </td>
                            <td>
                                <div class="actions-dropdown">
                                    <button class="actions-btn" onclick="toggleActionsDropdown(this)">Acciones ▼</button>
                                    <div class="actions-menu">
                                        <a href="#" onclick="editCenter(${centro.idCentro})">Editar</a>
                                        <c:if test="${centro.activo}">
                                            <a href="#" onclick="toggleCenterStatus(${centro.idCentro}, false)">Desactivar</a>
                                        </c:if>
                                        <c:if test="${!centro.activo}">
                                            <a href="#" onclick="toggleCenterStatus(${centro.idCentro}, true)">Activar</a>
                                        </c:if>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align: center; padding: 20px;">No se encontraron centros médicos</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </section>
</main>

<footer class="footer">
    <div class="footer-logo">
        <span style="font-weight: bold;">C</span>
    </div>
    <p>Sistema de vacunas desarrollado por estudiantes del<br>
        Instituto Universitario Cordillera</p>
</footer>

<script>
    function switchTab(tabName) {
        // Redirect to dashboard with tab parameter
        window.location.href = 'dashboard?tab=' + tabName;
    }

    function toggleUserDropdown() {
        const dropdown = document.getElementById('userDropdown');
        dropdown.classList.toggle('show');
    }

    function toggleActionsDropdown(button) {
        const menu = button.nextElementSibling;
        // Close all other dropdowns
        document.querySelectorAll('.actions-menu').forEach(m => {
            if (m !== menu) m.style.display = 'none';
        });
        // Toggle current dropdown
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    }

    function filterUsers(type) {
        const rows = document.querySelectorAll('#usuarios-section tbody tr[data-user-type]');
        rows.forEach(row => {
            const userType = row.getAttribute('data-user-type').toLowerCase();
            if (type === 'all' || userType.includes(type.slice(0, -1))) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    // Placeholder functions for modal operations
    function openEditProfileModal() { alert('Funcionalidad próximamente'); }
    function openCreateUserModal() { alert('Funcionalidad próximamente'); }
    function openCreateVaccineModal() { alert('Funcionalidad próximamente'); }
    function openCreateAppointmentModal() { alert('Funcionalidad próximamente'); }
    function openCreateCenterModal() { alert('Funcionalidad próximamente'); }
    function editUser(id) { alert('Editar usuario ' + id); }
    function editVaccine(id) { alert('Editar vacuna ' + id); }
    function editAppointment(id) { alert('Editar cita ' + id); }
    function editCenter(id) { alert('Editar centro ' + id); }
    function toggleUserStatus(id, block) { alert((block ? 'Bloquear' : 'Activar') + ' usuario ' + id); }
    function toggleCenterStatus(id, activate) { alert((activate ? 'Activar' : 'Desactivar') + ' centro ' + id); }
    function updateStock(id) { alert('Actualizar stock vacuna ' + id); }
    function cancelAppointment(id) { alert('Cancelar cita ' + id); }

    // Close dropdowns when clicking outside
    document.addEventListener('click', function(event) {
        if (!event.target.matches('.actions-btn')) {
            document.querySelectorAll('.actions-menu').forEach(menu => {
                menu.style.display = 'none';
            });
        }

        const userMenu = document.querySelector('.user-menu');
        const dropdown = document.getElementById('userDropdown');
        if (!userMenu.contains(event.target)) {
            dropdown.classList.remove('show');
        }
    });
</script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña - VACU</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<header class="header">
    <nav class="navbar">
        <a href="index.jsp" class="logo">VACU</a>
        <ul class="nav-links">
            <li><a href="#about">About</a></li>
            <li><a href="#learn">Learn</a></li>
            <li><a href="#maps">Maps</a></li>
        </ul>
        <a href="login.jsp" class="login-btn">Iniciar Sesion</a>
    </nav>
</header>

<main class="main-content">
    <div class="content-card" style="max-width: 600px;">
        <div class="form-container" style="width: 100%;">
            <h2 class="form-title">Recuperar Contraseña</h2>
            <p style="text-align: center; color: #666; margin-bottom: 30px;">
                Puedes recuperar tu contraseña por email o crear una nueva contraseña
            </p>

            <!-- Mostrar mensajes -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <!-- Tabs para elegir método de recuperación -->
            <div style="display: flex; margin-bottom: 20px; border-bottom: 1px solid #ddd;">
                <button type="button" class="tab-btn active" onclick="showTab('email')">
                    Recuperar por Email
                </button>
                <button type="button" class="tab-btn" onclick="showTab('reset')">
                    Crear Nueva Contraseña
                </button>
            </div>

            <!-- Formulario de recuperación por email -->
            <div id="emailTab" class="tab-content">
                <form action="ForgotPasswordServlet" method="post">
                    <input type="hidden" name="method" value="email">

                    <div class="form-group">
                        <label class="form-label">Correo Electrónico</label>
                        <input type="email" name="email" class="form-input"
                               placeholder="Ingresa tu email registrado" required>
                    </div>

                    <button type="submit" class="btn btn-primary">Enviar Nueva Contraseña</button>
                </form>
            </div>

            <!-- Formulario de creación de nueva contraseña -->
            <div id="resetTab" class="tab-content" style="display: none;">
                <form action="ForgotPasswordServlet" method="post" id="resetForm">
                    <input type="hidden" name="method" value="reset">

                    <div class="form-group">
                        <label class="form-label">Numero de Cedula</label>
                        <input type="text" name="cedula" class="form-input" placeholder="Num. Cedula"
                               required maxlength="10" pattern="[0-9]{10}">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Nueva Contraseña</label>
                        <input type="password" name="password" class="form-input"
                               placeholder="Nueva contraseña" required minlength="6">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Repetir Nueva Contraseña</label>
                        <input type="password" name="confirmPassword" class="form-input"
                               placeholder="Repetir contraseña" required minlength="6">
                    </div>

                    <button type="submit" class="btn btn-primary">Cambiar Contraseña</button>
                </form>
            </div>

            <div class="form-links">
                <p><a href="login.jsp">Volver al Login</a></p>
            </div>
        </div>
    </div>
</main>

<footer class="footer">
    <div class="footer-logo">
        <span style="font-weight: bold;">C</span>
    </div>
    <p>Sistema de vacunas desarrollado por estudiantes del<br>
        Instituto Universitario Cordillera</p>
</footer>

<style>
    .tab-btn {
        flex: 1;
        padding: 10px 20px;
        background: none;
        border: none;
        border-bottom: 2px solid transparent;
        cursor: pointer;
        font-size: 14px;
        color: #666;
    }

    .tab-btn.active {
        color: #667eea;
        border-bottom-color: #667eea;
    }

    .tab-content {
        margin-top: 20px;
    }
</style>

<script>
    function showTab(tabName) {
        // Ocultar todos los tabs
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.style.display = 'none';
        });

        // Remover clase active de todos los botones
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.classList.remove('active');
        });

        // Mostrar tab seleccionado
        document.getElementById(tabName + 'Tab').style.display = 'block';
        event.target.classList.add('active');
    }

    // Validación del formulario de reset
    document.getElementById('resetForm').addEventListener('submit', function(e) {
        const cedula = document.querySelector('input[name="cedula"]').value;
        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;

        // Validar cédula
        if (!/^[0-9]{10}$/.test(cedula)) {
            alert('La cédula debe tener exactamente 10 dígitos');
            e.preventDefault();
            return;
        }

        // Validar contraseña
        if (!/^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$/.test(password)) {
            alert('La contraseña debe tener al menos 6 caracteres con letras y números');
            e.preventDefault();
            return;
        }

        // Validar que las contraseñas coincidan
        if (password !== confirmPassword) {
            alert('Las contraseñas no coinciden');
            e.preventDefault();
            return;
        }
    });

    // Solo permitir números en el campo de cédula
    document.querySelector('input[name="cedula"]').addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
</script>
</body>
</html>

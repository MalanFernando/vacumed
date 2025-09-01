<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - VACU</title>
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
        <a href="register.jsp" class="login-btn">Registrarse</a>
    </nav>
</header>

<main class="main-content">
    <div class="content-card">
        <div class="content-left">
            <h1 class="main-title">Vacunación temprana</h1>
            <p class="subtitle">Gracias a la iniciativa de las instituciones de salud</p>

            <div style="margin-top: 40px;">
                <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
                    <span style="color: #999; font-weight: 500;">MSP</span>
                    <span style="color: #999; font-weight: 500;">ClinicaDoc</span>
                    <span style="color: #999; font-weight: 500;">HospitalMed</span>
                    <span style="color: #999; font-weight: 500;">Distrito Metropolitano de Quito</span>
                </div>
            </div>

            <!-- Ilustración -->
            <div style="text-align: center; margin-top: 30px;">
                <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Frame%2078-rT6oMABLCdG3gY8KGet851qMW4wZbv.png" alt="Ilustración de vacunación" class="illustration">
            </div>
        </div>

        <div class="content-right">
            <div class="form-container">
                <h2 class="form-title">Iniciar Sesion</h2>
                <p style="text-align: center; color: #666; margin-bottom: 20px;">Lorem ipsum Pretium donec ipsum.</p>

                <!-- Mostrar mensajes de error o éxito -->
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>

                <form action="LoginServlet" method="post" id="loginForm">
                    <div class="form-group">
                        <label class="form-label">Numero de Cedula</label>
                        <input type="text" name="cedula" class="form-input" placeholder="Num. Cedula"
                               value="${cedula}" required maxlength="10" pattern="[0-9]{10}">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Contraseña</label>
                        <input type="password" name="password" class="form-input" placeholder="Contraseña"
                               required minlength="6">
                    </div>

                    <button type="submit" class="btn btn-primary">Iniciar sesion</button>
                </form>

                <div class="form-links">
                    <p><a href="forgot-password.jsp">¿Olvidaste tu contraseña?</a></p>
                    <p>Aun no tienes una cuenta entonces puedes <a href="register.jsp">Registrarte</a></p>
                </div>
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

<script>
    // Validación del formulario en el cliente
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        const cedula = document.querySelector('input[name="cedula"]').value;
        const password = document.querySelector('input[name="password"]').value;

        // Validar cédula
        if (!/^[0-9]{10}$/.test(cedula)) {
            alert('La cédula debe tener exactamente 10 dígitos');
            e.preventDefault();
            return;
        }

        // Validar contraseña
        if (password.length < 6) {
            alert('La contraseña debe tener al menos 6 caracteres');
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

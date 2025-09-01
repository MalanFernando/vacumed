<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VACU - Sistema de Vacunación</title>
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
        </div>

        <div class="content-right">
            <div class="form-container">
                <h2 class="form-title">Iniciar Sesion</h2>
                <p style="text-align: center; color: #666; margin-bottom: 20px;">Lorem ipsum Pretium donec ipsum.</p>

                <form action="LoginServlet" method="post">
                    <div class="form-group">
                        <label class="form-label">Numero de Cedula</label>
                        <input type="text" name="cedula" class="form-input" placeholder="Num. Cedula" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Repetir contraseña</label>
                        <input type="password" name="password" class="form-input" placeholder="Contraseña" required>
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
</body>
</html>

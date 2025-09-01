<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrarse - VACU</title>
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

            <!-- Ilustración -->
            <div style="text-align: center; margin-top: 30px;">
                <img src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Frame%205-CHAdtCnA8aIh0ZHn18G2NR6gmdX6qj.png" alt="Ilustración de registro" class="illustration">
            </div>
        </div>

        <div class="content-right">
            <div class="form-container">
                <h2 class="form-title">Registrate</h2>
                <p style="text-align: center; color: #666; margin-bottom: 20px;">Lorem ipsum Pretium donec ipsum.</p>

                <!-- Mostrar mensajes de error -->
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form action="RegisterServlet" method="post" id="registerForm">
                    <div class="form-group">
                        <label class="form-label">Numero de Cedula</label>
                        <input type="text" name="cedula" class="form-input" placeholder="Num. Cedula"
                               value="${usuario.numCedula}" required maxlength="10" pattern="[0-9]{10}">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Nombre</label>
                        <input type="text" name="nombres" class="form-input" placeholder="Nombre"
                               value="${usuario.nombres}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Apellidos</label>
                        <input type="text" name="apellidos" class="form-input" placeholder="Apellidos"
                               value="${usuario.apellidos}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-input" placeholder="Email"
                               value="${usuario.email}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Teléfono</label>
                        <input type="tel" name="telefono" class="form-input" placeholder="Teléfono"
                               value="${usuario.telefono}">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Dirección</label>
                        <input type="text" name="direccion" class="form-input" placeholder="Dirección"
                               value="${usuario.direccion}">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Contraseña</label>
                        <input type="password" name="password" class="form-input" placeholder="Contraseña"
                               required minlength="6">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Repetir contraseña</label>
                        <input type="password" name="confirmPassword" class="form-input" placeholder="Repetir contraseña"
                               required minlength="6">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Indica si eres personal medico*</label>
                        <div class="radio-group">
                            <label>
                                <input type="radio" name="esPersonalMedico" value="si"> Si
                            </label>
                            <label>
                                <input type="radio" name="esPersonalMedico" value="no" checked> No
                            </label>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary">Registrarse</button>
                </form>

                <div class="form-links">
                    <p>Ya tienes una cuenta entonces puedes <a href="login.jsp">Iniciar Sesion</a></p>
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
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const cedula = document.querySelector('input[name="cedula"]').value;
        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;
        const email = document.querySelector('input[name="email"]').value;

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

        // Validar email
        if (!/^[A-Za-z0-9+_.-]+@(.+)$/.test(email)) {
            alert('Email inválido');
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

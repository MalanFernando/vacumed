-- Script para crear la base de datos y tablas iniciales
CREATE DATABASE IF NOT EXISTS vacunacion_ninos;
USE vacunacion_ninos;

-- Tabla: tipo_usuario
CREATE TABLE tipo_usuario (
    id_tipo_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre ENUM('ADMIN','MEDICO','PACIENTE') NOT NULL UNIQUE,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla: centro_de_salud
CREATE TABLE centro_de_salud (
    id_centro INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL, 
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla: usuarios
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    id_tipo_usuario INT NOT NULL,
    id_centro INT, 
    nombres VARCHAR(200) NOT NULL,
    apellidos VARCHAR(200) NOT NULL,
    num_cedula VARCHAR(10) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL, 
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    intentos_login INT DEFAULT 0,
    bloqueado BOOLEAN DEFAULT FALSE,
    fecha_bloqueo DATETIME NULL,
    FOREIGN KEY (id_tipo_usuario) REFERENCES tipo_usuario(id_tipo_usuario) ON DELETE RESTRICT,
    FOREIGN KEY (id_centro) REFERENCES centro_de_salud(id_centro) ON DELETE SET NULL
);

-- Tabla: ninos
CREATE TABLE ninos (
    id_nino INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL, 
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    num_cedula VARCHAR(10) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    sexo ENUM('M', 'F') NOT NULL,
    historia_clinica VARCHAR(6) UNIQUE NOT NULL,
    tipo_sangre ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') NOT NULL,
    direccion VARCHAR(200),
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla: info_medica
CREATE TABLE info_medica(
    id_info INT PRIMARY KEY AUTO_INCREMENT,
    id_nino INT NOT NULL UNIQUE,
    talla DECIMAL(5,2),
    peso DECIMAL(5,2),
    alergias TEXT,
    enfermedades TEXT,
    observaciones TEXT,
    FOREIGN KEY (id_nino) REFERENCES ninos(id_nino) ON DELETE CASCADE
);

-- Tabla: vacunas
CREATE TABLE vacunas (
    id_vacuna INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL, 
    descripcion TEXT, 
    codigo_internacional VARCHAR(20) UNIQUE,
    edad_recomendada_meses INT,
    dosis_requeridas INT NOT NULL,
    stock INT DEFAULT 0,
    fecha_expiracion DATE,
    lote VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla: dosis
CREATE TABLE dosis (
    id_dosis INT PRIMARY KEY AUTO_INCREMENT,
    id_nino INT NOT NULL, 
    id_vacuna INT NOT NULL,
    id_centro INT NOT NULL, 
    id_enfermero INT NOT NULL, 
    fecha_aplicacion DATE NOT NULL,
    lote_vacuna VARCHAR(50),
    numero_dosis INT NOT NULL,  
    recordatorio_enviado BOOLEAN DEFAULT FALSE,
    fecha_proxima_dosis DATE,
    observaciones TEXT,
    FOREIGN KEY (id_nino) REFERENCES ninos(id_nino) ON DELETE CASCADE,
    FOREIGN KEY (id_vacuna) REFERENCES vacunas(id_vacuna) ON DELETE RESTRICT,
    FOREIGN KEY (id_centro) REFERENCES centro_de_salud(id_centro) ON DELETE RESTRICT,
    FOREIGN KEY (id_enfermero) REFERENCES usuarios(id_usuario) ON DELETE RESTRICT,
    UNIQUE KEY uk_dosis_nino_vacuna (id_nino, id_vacuna, numero_dosis)
);

-- Tabla: citas
CREATE TABLE citas (
    id_cita INT PRIMARY KEY AUTO_INCREMENT,
    id_nino INT NOT NULL,
    id_centro INT NOT NULL,
    id_vacuna INT,
    fecha_cita DATETIME NOT NULL,
    motivo VARCHAR(200),
    estado ENUM('PENDIENTE','ATENDIDA','CANCELADA') DEFAULT 'PENDIENTE',
    observaciones TEXT,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_nino) REFERENCES ninos(id_nino) ON DELETE CASCADE,
    FOREIGN KEY (id_centro) REFERENCES centro_de_salud(id_centro) ON DELETE RESTRICT,
    FOREIGN KEY (id_vacuna) REFERENCES vacunas(id_vacuna) ON DELETE SET NULL
);

-- Tabla: recuperacion_contrasena
CREATE TABLE recuperacion_contrasena (
    id_recuperacion INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL, 
    token VARCHAR(255) NOT NULL UNIQUE, 
    fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP, 
    fecha_expiracion DATETIME NOT NULL, 
    usado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla: auditoria
CREATE TABLE auditoria (
    id_auditoria INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    accion VARCHAR(100) NOT NULL, 
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE RESTRICT
);

-- Tabla: intentos_login
CREATE TABLE intentos_login (
    id_intento INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT,
    email VARCHAR(100),
    fecha_intento DATETIME DEFAULT CURRENT_TIMESTAMP,
    exitoso BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

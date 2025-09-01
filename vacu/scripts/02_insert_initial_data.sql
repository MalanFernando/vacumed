-- Insertar datos iniciales
USE vacunacion_ninos;

-- Insertar tipos de usuario
INSERT INTO tipo_usuario (nombre) VALUES 
('ADMIN'),
('MEDICO'), 
('PACIENTE');

-- Insertar centros de salud
INSERT INTO centro_de_salud (nombre, direccion, telefono, email) VALUES
('Centro Norte', 'Direccion uno', '0965874132', 'centro@hotmail.com'),
('Centro Sur', 'Direccion dos', '0987564123', 'sur@gmail.com'),
('Quito Centro', 'Av. 10 de Agosto y Colón', '0987654321', 'quitocentro@salud.gob.ec'),
('Quito Norte', 'Av. Eloy Alfaro y Granados', '0987654322', 'quitonorte@salud.gob.ec');

-- Insertar usuario administrador por defecto
INSERT INTO usuarios (id_tipo_usuario, nombres, apellidos, num_cedula, email, contrasena, telefono, direccion) VALUES
(1, 'Administrador', 'Sistema', '1234567890', 'admin@sistema.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZJLjqOm9coFZcQcob2P.VnO/S8Oa', '0999999999', 'Quito, Ecuador');

-- Insertar usuarios de ejemplo
INSERT INTO usuarios (id_tipo_usuario, id_centro, nombres, apellidos, num_cedula, email, contrasena, telefono, direccion) VALUES
(2, 1, 'Amanda', 'Heredia', '1122334455', 'amanda@medico.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZJLjqOm9coFZcQcob2P.VnO/S8Oa', '0987654321', 'Quito Norte'),
(3, 2, 'Fernando', 'Malan', '1234567890', 'fernando@paciente.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZJLjqOm9coFZcQcob2P.VnO/S8Oa', '0987654322', 'Quito Sur');

-- Insertar vacunas de ejemplo
INSERT INTO vacunas (nombre, descripcion, codigo_internacional, edad_recomendada_meses, dosis_requeridas, stock, fecha_expiracion, lote) VALUES
('Hepatitis B', 'Vacuna contra Hepatitis B', 'XCBN0022', 0, 3, 0, '2025-08-09', 'LOTE001'),
('Rubiola', 'Vacuna contra Rubiola', 'ZDBN0045', 12, 2, 12, '2025-10-09', 'LOTE002'),
('BCG', 'Vacuna contra Tuberculosis', 'BCG001', 0, 1, 25, '2025-12-15', 'LOTE003'),
('Polio', 'Vacuna contra Poliomielitis', 'POLIO001', 2, 4, 30, '2025-11-20', 'LOTE004');

-- Insertar niños de ejemplo
INSERT INTO ninos (id_usuario, nombres, apellidos, num_cedula, fecha_nacimiento, sexo, historia_clinica, tipo_sangre, direccion) VALUES
(3, 'Carlos', 'Malan', '1234567891', '2023-01-15', 'M', '001234', 'O+', 'Quito Sur'),
(3, 'Maria', 'Malan', '1234567892', '2022-06-20', 'F', '001235', 'A+', 'Quito Sur');

-- Insertar citas de ejemplo
INSERT INTO citas (id_nino, id_centro, id_vacuna, fecha_cita, motivo, estado) VALUES
(1, 1, 1, '2025-09-08 10:00:00', 'Vacunación Hepatitis B', 'PENDIENTE'),
(2, 2, 2, '2025-12-09 14:30:00', 'Vacunación Rubiola', 'PENDIENTE');

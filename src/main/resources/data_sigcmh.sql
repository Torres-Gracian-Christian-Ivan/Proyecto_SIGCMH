-- =========================================================
-- SIGCMH - data.sql
-- Datos iniciales + datos de demostración
-- =========================================================

SET NAMES utf8mb4;

-- =========================================================
-- CATÁLOGOS
-- =========================================================

INSERT INTO perfil (nombre, descripcion, activo) VALUES
('Administrador','Control total del sistema',TRUE),
('Recepcionista','Gestión de citas y pacientes',TRUE);

INSERT INTO genero (nombre, activo) VALUES
('Masculino',TRUE),
('Femenino',TRUE),
('Otro',TRUE);

INSERT INTO estado_cita (nombre, activo) VALUES
('Programada',TRUE),
('Confirmada',TRUE),
('Atendida',TRUE),
('Cancelada',TRUE);

INSERT INTO especialidad (nombre, descripcion) VALUES
('Medicina General','Consulta médica general'),
('Cardiología','Enfermedades del corazón'),
('Dermatología','Piel, cabello y uñas'),
('Pediatría','Atención infantil'),
('Traumatología','Lesiones óseas y musculares'),
('Ginecología','Salud femenina');

-- =========================================================
-- CONSULTORIOS
-- 3 por piso. Cada piso se dedica a una especialidad actual.
-- Piso 7 queda reservado para especialidades futuras.
-- =========================================================

INSERT INTO consultorio (numero, ubicacion, activo) VALUES
('101','Piso 1 - Medicina General',TRUE),
('102','Piso 1 - Medicina General',TRUE),
('103','Piso 1 - Medicina General',TRUE),

('201','Piso 2 - Cardiología',TRUE),
('202','Piso 2 - Cardiología',TRUE),
('203','Piso 2 - Cardiología',TRUE),

('301','Piso 3 - Dermatología',TRUE),
('302','Piso 3 - Dermatología',TRUE),
('303','Piso 3 - Dermatología',TRUE),

('401','Piso 4 - Pediatría',TRUE),
('402','Piso 4 - Pediatría',TRUE),
('403','Piso 4 - Pediatría',TRUE),

('501','Piso 5 - Traumatología',TRUE),
('502','Piso 5 - Traumatología',TRUE),
('503','Piso 5 - Traumatología',TRUE),

('601','Piso 6 - Ginecología',TRUE),
('602','Piso 6 - Ginecología',TRUE),
('603','Piso 6 - Ginecología',TRUE),

('701','Piso 7 - Otras especialidades',TRUE),
('702','Piso 7 - Otras especialidades',TRUE),
('703','Piso 7 - Otras especialidades',TRUE);

-- =========================================================
-- USUARIOS
-- Credenciales de demostración:
-- admin@sigcmh.com / Admin123!
-- laura.mendez@sigcmh.com / Recep123!
-- carlos.vega@sigcmh.com / Recep456!
-- =========================================================

INSERT INTO usuario
(email,password_hash,nombre_completo,activo,id_perfil)
VALUES
('admin@sigcmh.com','$2y$10$Mxv2hFO8EydAiyNDGT203Ox2wGJGyZG7c3/TFzWmhk/HWjuDk4gdW','Administrador General',TRUE,1),
('laura.mendez@sigcmh.com','$2y$10$6W2df6Zv83NeNGoefWl5Z.3Sfw6pNMdsrmNMoz8qcMInEipoPwPg6','Laura Méndez',TRUE,2),
('carlos.vega@sigcmh.com','$2y$10$V65KOyehaeG8SDHoVM0sL.wXEa8tE2i0qrTTaLCNz3dLA8eAqf0O2','Carlos Vega',TRUE,2);

-- =========================================================
-- PACIENTES
-- =========================================================

INSERT INTO paciente
(nombre_completo,numero_identificacion,fecha_nacimiento,id_genero,telefono,email,direccion,activo)
VALUES
('José Martínez','PAC001','1995-05-12',1,'5511111111','jose.martinez@gmail.com','Av. Universidad 100',TRUE),
('Luis Hernández','PAC002','1992-11-20',1,'5522222222','luis.hernandez@gmail.com','Coyoacán 25',TRUE),
('Miguel Ortega','PAC003','1988-03-10',1,'5533333333','miguel.ortega@gmail.com','Del Valle 80',TRUE),
('Carlos Flores','PAC004','1999-01-18',1,'5544444444','carlos.flores@gmail.com','Narvarte 45',TRUE),
('Ana Gómez','PAC005','1997-08-04',2,'5555555555','ana.gomez@gmail.com','Roma Norte 12',TRUE),
('Fernanda Ríos','PAC006','1994-02-15',2,'5566666666','fernanda.rios@gmail.com','Condesa 78',TRUE),
('María Sánchez','PAC007','1985-09-30',2,'5577777777','maria.sanchez@gmail.com','Insurgentes 230',TRUE),
('Patricia Cruz','PAC008','1990-12-01',2,'5588888888','patricia.cruz@gmail.com','Copilco 15',TRUE),
('Jorge Ramírez','PAC009','1978-07-11',1,'5599999999','jorge.ramirez@gmail.com','Tlalpan 510',TRUE),
('Daniela Torres','PAC010','2001-06-22',2,'5510101010','daniela.torres@gmail.com','Portales 60',TRUE),
('Mariana Romero','PAC011','1996-01-17',2,'5582000001','mariana.romero@gmail.com','Mixcoac 120',TRUE),
('Ricardo Velázquez','PAC012','1985-04-21',1,'5582000002','ricardo.velazquez@gmail.com','San Ángel 44',TRUE),
('Paola Jiménez','PAC013','2000-10-03',2,'5582000003','paola.jimenez@gmail.com','Nápoles 88',TRUE),
('Andrés Morales','PAC014','1991-07-11',1,'5582000004','andres.morales@gmail.com','Escandón 30',TRUE),
('Camila Ponce','PAC015','2013-12-08',2,'5582000005','camila.ponce@gmail.com','Pedregal 17',TRUE),
('Mauricio Salas','PAC016','1974-08-02',1,'5582000006','mauricio.salas@gmail.com','Centro 205',TRUE),
('Karla Estrada','PAC017','2002-01-31',2,'5582000007','karla.estrada@gmail.com','Lindavista 61',TRUE),
('Daniel Rangel','PAC018','1988-11-12',1,'5582000008','daniel.rangel@gmail.com','Iztacalco 72',TRUE),
('Ximena Soto','PAC019','2016-06-19',2,'5582000009','ximena.soto@gmail.com','Taxqueña 33',TRUE),
('Gabriel Luna','PAC020','1999-02-22',1,'5582000010','gabriel.luna@gmail.com','Doctores 14',TRUE),
('Nora Delgado','PAC021','1978-03-05',2,'5582000011','nora.delgado@gmail.com','Álamos 39',TRUE),
('Bruno Campos','PAC022','2006-10-12',1,'5582000012','bruno.campos@gmail.com','Acoxpa 71',TRUE),
('Cecilia Padilla','PAC023','1990-09-26',2,'5582000013','cecilia.padilla@gmail.com','Roma Sur 104',TRUE),
('Sebastián Mejía','PAC024','2015-04-15',1,'5582000014','sebastian.mejia@gmail.com','Coapa 18',TRUE),
('Ivana Rosales','PAC025','1995-05-14',2,'5582000015','ivana.rosales@gmail.com','Santa Úrsula 52',TRUE),
('Emilio Ruiz','PAC026','1979-08-20',1,'5582000016','emilio.ruiz@gmail.com','Anzures 90',TRUE),
('Renata Díaz','PAC027','2014-09-21',2,'5582000017','renata.diaz@gmail.com','Xoco 37',TRUE),
('Víctor Guzmán','PAC028','1983-02-06',1,'5582000018','victor.guzman@gmail.com','Tacubaya 65',TRUE),
('Sofía Vargas','PAC029','2005-07-23',2,'5582000019','sofia.vargas@gmail.com','Villa Coapa 49',TRUE),
('Héctor Fuentes','PAC030','1967-01-09',1,'5582000020','hector.fuentes@gmail.com','Moctezuma 111',TRUE);

-- =========================================================
-- MÉDICOS
-- 3 médicos por especialidad y 1 por consultorio principal.
-- =========================================================

INSERT INTO medico
(nombre_completo,numero_identificacion,telefono,email,id_especialidad,id_consultorio,activo)
VALUES
('Dr. Juan Pérez','MED001','5512340001','juan.perez@sigcmh.com',1,1,TRUE),
('Dr. Roberto Medina','MED002','5512340002','roberto.medina@sigcmh.com',1,2,TRUE),
('Dra. Elena Navarro','MED003','5512340003','elena.navarro@sigcmh.com',1,3,TRUE),
('Dra. Ana López','MED004','5512340004','ana.lopez@sigcmh.com',2,4,TRUE),
('Dr. Ignacio Castro','MED005','5512340005','ignacio.castro@sigcmh.com',2,5,TRUE),
('Dra. Valeria Soto','MED006','5512340006','valeria.soto@sigcmh.com',2,6,TRUE),
('Dr. Carlos Ramírez','MED007','5512340007','carlos.ramirez@sigcmh.com',3,7,TRUE),
('Dra. Melissa Ortega','MED008','5512340008','melissa.ortega@sigcmh.com',3,8,TRUE),
('Dr. Diego Campos','MED009','5512340009','diego.campos@sigcmh.com',3,9,TRUE),
('Dra. Sofía Herrera','MED010','5512340010','sofia.herrera@sigcmh.com',4,10,TRUE),
('Dr. Eduardo Molina','MED011','5512340011','eduardo.molina@sigcmh.com',4,11,TRUE),
('Dra. Natalia Ríos','MED012','5512340012','natalia.rios@sigcmh.com',4,12,TRUE),
('Dr. Miguel Torres','MED013','5512340013','miguel.torres@sigcmh.com',5,13,TRUE),
('Dra. Claudia Vázquez','MED014','5512340014','claudia.vazquez@sigcmh.com',5,14,TRUE),
('Dr. Raúl Mendoza','MED015','5512340015','raul.mendoza@sigcmh.com',5,15,TRUE),
('Dra. Fernanda Ruiz','MED016','5512340016','fernanda.ruiz@sigcmh.com',6,16,TRUE),
('Dra. Adriana Paredes','MED017','5512340017','adriana.paredes@sigcmh.com',6,17,TRUE),
('Dr. Sergio Luna','MED018','5512340018','sergio.luna@sigcmh.com',6,18,TRUE);

-- =========================================================
-- HORARIOS DE ATENCIÓN
-- Para la demostración todos los médicos atienden de lunes a
-- domingo de 08:00 a 16:00. El sistema puede modificarlos después.
-- dia_semana: 1=Lunes ... 7=Domingo
-- =========================================================

INSERT INTO horario_atencion
(id_medico,dia_semana,hora_inicio,hora_fin,activo)
SELECT
    m.id,
    d.dia,
    '08:00:00',
    '16:00:00',
    TRUE
FROM medico m
CROSS JOIN (
    SELECT 1 AS dia
    UNION ALL SELECT 2
    UNION ALL SELECT 3
    UNION ALL SELECT 4
    UNION ALL SELECT 5
    UNION ALL SELECT 6
    UNION ALL SELECT 7
) d;

-- =========================================================
-- CITAS HISTÓRICAS
-- Conservan la idea del data.sql original.
-- =========================================================

INSERT INTO cita
(id_paciente,id_medico,id_usuario_creador,fecha_hora,id_estado_cita,motivo_consulta,observaciones)
VALUES
(1,1,2,'2026-08-01 09:00:00',1,'Consulta general','Primera consulta'),
(2,4,2,'2026-08-01 10:00:00',2,'Dolor de pecho','Paciente estable'),
(3,7,3,'2026-08-02 11:00:00',3,'Revisión dermatológica','Tratamiento exitoso'),
(4,10,2,'2026-08-02 12:00:00',4,'Control pediátrico','Cancelada por paciente'),
(5,13,3,'2026-08-03 09:30:00',1,'Lesión deportiva','Pendiente de valoración'),
(6,16,2,'2026-08-03 11:00:00',2,'Consulta ginecológica','Confirmada'),
(7,1,3,'2026-08-04 08:30:00',3,'Chequeo anual','Sin observaciones'),
(8,4,2,'2026-08-04 13:00:00',1,'Hipertensión','Seguimiento'),
(9,7,3,'2026-08-05 10:30:00',2,'Alergia cutánea','Medicamento recetado'),
(10,10,2,'2026-08-05 15:00:00',1,'Consulta pediátrica','Primera visita'),
(1,13,3,'2026-08-06 09:00:00',4,'Dolor de rodilla','Reprogramar'),
(2,16,2,'2026-08-06 12:00:00',3,'Control médico','Alta médica');

-- =========================================================
-- CITAS DE DEMOSTRACIÓN
--
-- 6 semanas consecutivas, una por cada especialidad:
-- Semana 1: Medicina General
-- Semana 2: Cardiología
-- Semana 3: Dermatología
-- Semana 4: Pediatría
-- Semana 5: Traumatología
-- Semana 6: Ginecología
--
-- 3 citas por día durante 7 días = 21 por semana.
-- Total: 126 citas de demostración.
-- =========================================================

INSERT INTO cita
(id_paciente,id_medico,id_usuario_creador,fecha_hora,id_estado_cita,motivo_consulta,observaciones)
SELECT
    MOD(nums.n * 7, 30) + 1 AS id_paciente,

    (FLOOR(nums.n / 21) * 3)
        + MOD(nums.n, 3)
        + 1 AS id_medico,

    CASE
        WHEN MOD(nums.n, 2) = 0 THEN 2
        ELSE 3
    END AS id_usuario_creador,

    TIMESTAMP(
        DATE_ADD(
            '2026-10-05',
            INTERVAL FLOOR(nums.n / 3) DAY
        ),
        CASE MOD(nums.n, 3)
            WHEN 0 THEN '09:00:00'
            WHEN 1 THEN '11:00:00'
            ELSE '13:00:00'
        END
    ) AS fecha_hora,

    CASE
        WHEN MOD(nums.n, 10) = 9 THEN 4
        WHEN MOD(nums.n, 2) = 0 THEN 1
        ELSE 2
    END AS id_estado_cita,

    CASE FLOOR(nums.n / 21)

        WHEN 0 THEN
            CASE MOD(nums.n, 3)
                WHEN 0 THEN 'Consulta general'
                WHEN 1 THEN 'Chequeo preventivo'
                ELSE 'Seguimiento médico'
            END

        WHEN 1 THEN
            CASE MOD(nums.n, 3)
                WHEN 0 THEN 'Valoración cardiológica'
                WHEN 1 THEN 'Control de presión arterial'
                ELSE 'Seguimiento cardiovascular'
            END

        WHEN 2 THEN
            CASE MOD(nums.n, 3)
                WHEN 0 THEN 'Revisión dermatológica'
                WHEN 1 THEN 'Alergia cutánea'
                ELSE 'Seguimiento de tratamiento dermatológico'
            END

        WHEN 3 THEN
            CASE MOD(nums.n, 3)
                WHEN 0 THEN 'Consulta pediátrica'
                WHEN 1 THEN 'Control de crecimiento'
                ELSE 'Seguimiento infantil'
            END

        WHEN 4 THEN
            CASE MOD(nums.n, 3)
                WHEN 0 THEN 'Dolor articular'
                WHEN 1 THEN 'Valoración de lesión deportiva'
                ELSE 'Seguimiento traumatológico'
            END

        ELSE
            CASE MOD(nums.n, 3)
                WHEN 0 THEN 'Consulta ginecológica'
                WHEN 1 THEN 'Control preventivo'
                ELSE 'Seguimiento ginecológico'
            END

    END AS motivo_consulta,

    CASE
        WHEN MOD(nums.n, 10) = 9
            THEN 'Cita cancelada por el paciente'
        WHEN MOD(nums.n, 2) = 0
            THEN 'Cita programada'
        ELSE 'Cita confirmada'
    END AS observaciones

FROM (
    SELECT
        unidades.n + decenas.n * 10 + centenas.n * 100 AS n
    FROM (
        SELECT 0 AS n
        UNION ALL SELECT 1
        UNION ALL SELECT 2
        UNION ALL SELECT 3
        UNION ALL SELECT 4
        UNION ALL SELECT 5
        UNION ALL SELECT 6
        UNION ALL SELECT 7
        UNION ALL SELECT 8
        UNION ALL SELECT 9
    ) unidades
    CROSS JOIN (
        SELECT 0 AS n
        UNION ALL SELECT 1
        UNION ALL SELECT 2
        UNION ALL SELECT 3
        UNION ALL SELECT 4
        UNION ALL SELECT 5
        UNION ALL SELECT 6
        UNION ALL SELECT 7
        UNION ALL SELECT 8
        UNION ALL SELECT 9
    ) decenas
    CROSS JOIN (
        SELECT 0 AS n
        UNION ALL SELECT 1
    ) centenas
) nums
WHERE nums.n < 126
ORDER BY nums.n;

-- =========================================================
-- COMPROBACIONES
-- =========================================================

SELECT COUNT(*) AS total_pacientes
FROM paciente;

SELECT COUNT(*) AS total_medicos
FROM medico;

SELECT COUNT(*) AS total_consultorios
FROM consultorio;

SELECT COUNT(*) AS total_horarios
FROM horario_atencion;

SELECT COUNT(*) AS total_citas
FROM cita;

SELECT
    e.nombre AS especialidad,
    co.ubicacion,
    COUNT(c.id) AS total_citas
FROM cita c
JOIN medico m
    ON m.id = c.id_medico
JOIN especialidad e
    ON e.id = m.id_especialidad
JOIN consultorio co
    ON co.id = m.id_consultorio
WHERE c.fecha_hora >= '2026-10-05'
GROUP BY e.nombre, co.ubicacion
ORDER BY MIN(c.fecha_hora), co.numero;


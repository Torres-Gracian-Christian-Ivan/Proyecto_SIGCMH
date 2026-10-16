-- =========================================================
-- SIGCMH - schema.sql
-- Sistema de Gestión de Citas Médicas Hospitalarias
-- Proyecto Final - Módulo 9
-- =========================================================

SET NAMES utf8mb4;
-- =========================================================
-- REINICIO OPCIONAL
-- Si quieres reconstruir completamente la BD, ejecuta primero
-- manualmente:
--
-- DROP DATABASE IF EXISTS sigcmh;
--CREATE DATABASE sigcmh
--CHARACTER SET utf8mb4
--COLLATE utf8mb4_unicode_ci;

USE sigcmh;
--
-- Después inicia Spring una sola vez con sql.init.mode=always.
-- =========================================================

CREATE TABLE perfil (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(150),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE genero (
    id_genero INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE estado_cita (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE especialidad (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

CREATE TABLE consultorio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(20) NOT NULL UNIQUE,
    ubicacion VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    id_perfil INT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_perfil
        FOREIGN KEY (id_perfil)
        REFERENCES perfil(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE paciente (
    id_paciente INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    numero_identificacion VARCHAR(30) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    id_genero INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE,
    direccion VARCHAR(150),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_paciente_genero
        FOREIGN KEY (id_genero)
        REFERENCES genero(id_genero)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE medico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    numero_identificacion VARCHAR(30) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    id_especialidad INT NOT NULL,
    id_consultorio INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_medico_especialidad
        FOREIGN KEY (id_especialidad)
        REFERENCES especialidad(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_medico_consultorio
        FOREIGN KEY (id_consultorio)
        REFERENCES consultorio(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE horario_atencion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_medico INT NOT NULL,
    dia_semana TINYINT NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_horario_medico
        FOREIGN KEY (id_medico)
        REFERENCES medico(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_dia_semana
        CHECK (dia_semana BETWEEN 1 AND 7),

    CONSTRAINT chk_hora_rango
        CHECK (hora_inicio < hora_fin),

    CONSTRAINT uk_horario_medico
        UNIQUE (id_medico, dia_semana, hora_inicio)
);

CREATE TABLE cita (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    id_usuario_creador INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    id_estado_cita INT NOT NULL,
    motivo_consulta VARCHAR(150) NOT NULL,
    observaciones VARCHAR(255),
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cita_paciente
        FOREIGN KEY (id_paciente)
        REFERENCES paciente(id_paciente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_cita_medico
        FOREIGN KEY (id_medico)
        REFERENCES medico(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_cita_usuario
        FOREIGN KEY (id_usuario_creador)
        REFERENCES usuario(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_cita_estado
        FOREIGN KEY (id_estado_cita)
        REFERENCES estado_cita(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT uk_cita_medico
        UNIQUE (id_medico, fecha_hora),

    CONSTRAINT uk_cita_paciente
        UNIQUE (id_paciente, fecha_hora)
);

-- Índices útiles para búsquedas y reportes.
CREATE INDEX idx_cita_fecha_hora
    ON cita(fecha_hora);

CREATE INDEX idx_cita_estado_fecha
    ON cita(id_estado_cita, fecha_hora);

CREATE INDEX idx_medico_especialidad
    ON medico(id_especialidad);

CREATE INDEX idx_medico_consultorio
    ON medico(id_consultorio);


CREATE TABLE refresh_token (
    id INT PRIMARY KEY AUTO_INCREMENT,

    token VARCHAR(100) NOT NULL UNIQUE,

    fecha_expiracion DATETIME NOT NULL,

    revocado BOOLEAN NOT NULL DEFAULT FALSE,

    id_usuario INT NOT NULL UNIQUE,

    CONSTRAINT fk_refresh_token_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

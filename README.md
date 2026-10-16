# SIGCMH - Sistema de Gestión de Citas Médicas Hospitalarias

Proyecto final desarrollado con Spring Boot para la administración de citas médicas hospitalarias.

El sistema permite gestionar pacientes, médicos, horarios de atención, citas, usuarios y reportes. Como parte de la integración final se implementó **Spring Security con JWT, Refresh Token, cookies, roles y autorización por perfiles**.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.x
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- Bootstrap
- MariaDB
- Maven
- JWT con JJWT 0.12.5
- OpenPDF
- Lombok

---

## Módulos principales

- Usuarios
- Pacientes
- Médicos
- Especialidades
- Consultorios
- Horarios de atención
- Citas médicas
- Estados de cita
- Reportes
- Agenda semanal por consultorio
- Generación de reportes PDF

---

## Seguridad

El proyecto utiliza Spring Security para controlar autenticación y autorización.

### Autenticación

El usuario inicia sesión mediante correo electrónico y contraseña.

Las contraseñas se almacenan utilizando **BCrypt**, por lo que no se guardan en texto plano.

Después de una autenticación correcta se generan:

- `access_token`
- `refresh_token`

Ambos se almacenan en cookies.

El `access_token` identifica al usuario y sus roles y se utiliza para autenticar las peticiones protegidas.

El `refresh_token` permite generar un nuevo Access Token cuando el anterior expira. También se almacena en la base de datos para validar su vigencia, rotarlo y revocarlo durante el logout.

### Protección general

La aplicación protege por defecto cualquier ruta que no haya sido declarada explícitamente como pública.

Si un usuario intenta entrar directamente a un módulo sin haber iniciado sesión, Spring Security lo redirige al formulario de login.

La autenticación está configurada como `STATELESS`, por lo que Spring Security no utiliza la sesión HTTP para conservar la identidad autenticada.

Los formularios Thymeleaf mantienen protección CSRF.

---

## Roles y permisos

### ADMINISTRADOR

Puede:

- administrar usuarios;
- crear, editar y consultar médicos;
- administrar horarios de atención;
- administrar pacientes;
- administrar citas;
- consultar reportes;
- generar PDF.

### RECEPCIONISTA

Puede:

- consultar médicos;
- consultar horarios;
- administrar pacientes;
- administrar citas;
- consultar reportes.

No puede:

- acceder al módulo de usuarios;
- crear o modificar médicos;
- modificar horarios administrativos.

Si un usuario autenticado intenta acceder a una operación para la cual no tiene permisos, Spring Security responde con **403 Forbidden**.

Para la interfaz web se utiliza una página personalizada de acceso denegado. Para la API se devuelve una respuesta JSON con código HTTP 403.

---

## Componentes principales de seguridad

### `SecurityConfiguration`

Define rutas públicas, rutas protegidas, permisos por roles, login, logout, política de sesiones, CSRF, filtro JWT y manejo de accesos denegados.

### `CustomUserDetailsService`

Obtiene al usuario desde la base de datos mediante su correo electrónico y convierte su perfil en una autoridad de Spring Security.

Ejemplos:

```text
ROLE_ADMINISTRADOR
ROLE_RECEPCIONISTA
```

### `JwtService`

Se encarga de generar, firmar, leer y validar los JWT.

### `JwtAuthenticationFilter`

Intercepta las peticiones protegidas.

Puede obtener el Access Token desde:

- `Authorization: Bearer ...`
- cookie `access_token`

Si el token es válido, establece la autenticación dentro del `SecurityContext`.

Si el Access Token ya no está disponible y existe un Refresh Token válido, puede renovar la autenticación.

### `JwtCookieService`

Administra las cookies:

```text
access_token
refresh_token
```

Las cookies JWT se configuran como `HttpOnly`.

### `RefreshTokenService`

Se encarga de crear, validar, rotar y revocar Refresh Tokens.

### `CustomAuthenticationSuccessHandler`

Después de un login correcto genera el Access Token, crea o reemplaza el Refresh Token, agrega ambas cookies y redirige al usuario al sistema.

### `CustomLogoutHandler`

Durante el logout revoca el Refresh Token y elimina las cookies JWT.

### `CustomAccessDeniedHandler`

Maneja accesos no autorizados.

- Web: redirige a una página personalizada.
- API: devuelve `403 Forbidden` en JSON.

---

## Requisitos

- JDK 17
- Maven
- MariaDB
- IntelliJ IDEA, Eclipse, VS Code o IDE compatible con Spring Boot

Configuración utilizada durante el desarrollo:

```text
Servidor Spring Boot: 8090
MariaDB: 3309
Base de datos: sigcmh
```

---

## Base de datos

Los scripts se encuentran en:

```text
src/main/resources/schema_sigcmh.sql
src/main/resources/data_sigcmh.sql
```

Ejecutar primero:

```text
schema_sigcmh.sql
```

y después:

```text
data_sigcmh.sql
```

El esquema contiene, entre otras, las tablas:

```text
perfil
genero
estado_cita
especialidad
consultorio
usuario
paciente
medico
horario_atencion
cita
refresh_token
```

Después de inicializar la base de datos se recomienda mantener:

```yaml
spring:
  sql:
    init:
      mode: never
```

---

## Variables de entorno

El proyecto no almacena directamente la contraseña de la base de datos ni la clave secreta del JWT.

Se deben definir:

```text
DB_PASSWORD
JWT_SECRET
```

Ejemplo:

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD}

security:
  jwt:
    secret: ${JWT_SECRET}
```

---

## Configuración JWT

Ejemplo de configuración:

```yaml
security:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 900000
    refresh-expiration: 604800000
```

Valores utilizados:

```text
Access Token: 15 minutos
Refresh Token: 7 días
```

---

## Ejecución

### Desde IntelliJ IDEA

1. Configurar `DB_PASSWORD`.
2. Configurar `JWT_SECRET`.
3. Verificar que MariaDB esté iniciado.
4. Verificar que exista la base `sigcmh`.
5. Ejecutar `ProyectoFinalSigcmhApplication`.
6. Abrir:

```text
http://localhost:8090
```

Si no existe una autenticación válida, Spring Security redirigirá automáticamente a:

```text
http://localhost:8090/login
```

### Desde Maven

```bash
mvn spring-boot:run
```

---

## API de autenticación

### Login

```http
POST /api/auth/login
```

Ejemplo:

```json
{
  "email": "usuario@ejemplo.com",
  "password": "contraseña"
}
```

Respuesta esperada:

```json
{
  "accessToken": "...",
  "refreshToken": "...",
  "tokenType": "Bearer"
}
```

### Refresh Token

```http
POST /api/auth/refresh
```

Si el Refresh Token es válido:

- se genera un Access Token nuevo;
- se rota el Refresh Token;
- se actualizan las cookies.

Si está expirado o revocado:

```text
401 Unauthorized
```

---

## Uso con Postman

Para consumir endpoints protegidos:

```http
Authorization: Bearer ACCESS_TOKEN
```

Ejemplo:

```http
GET http://localhost:8090/api/medicos
```

Un usuario con permisos recibe:

```text
200 OK
```

Un usuario autenticado sin permisos recibe:

```text
403 Forbidden
```

Ejemplo de respuesta:

```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "No tienes permisos para realizar esta operación."
}
```

---

## Funcionalidades destacadas

### Pacientes

Permite listar, consultar, registrar y editar pacientes.

### Médicos

Permite listar, consultar, registrar y editar médicos, además de asociar especialidad y consultorio.

### Horarios de atención

Permite registrar horarios asociados a cada médico y valida que no existan horarios traslapados.

### Citas

Permite relacionar paciente, médico, estado, fecha y hora, motivo de consulta, observaciones y usuario creador.

### Usuarios

CRUD administrativo de usuarios.

Permite:

- listar;
- consultar;
- crear;
- editar;
- asignar perfiles;
- cambiar contraseña;
- activar o desactivar usuarios.

Las contraseñas se almacenan con BCrypt.

### Reportes

Permite consultar una agenda semanal por consultorio y generar un reporte en PDF.

---

## Estructura general

```text
edu.unam
│
├── auth
│   ├── controller
│   ├── mapper
│   ├── model
│   ├── repository
│   └── service
│
├── common
│   ├── controller
│   ├── exception
│   └── response
│
├── security
│   ├── dto
│   ├── handler
│   ├── jwt
│   ├── logout
│   ├── model
│   ├── request
│   └── service
│
└── system
    ├── catalogo
    ├── cita
    ├── estadocita
    ├── horarioatencion
    ├── medico
    ├── paciente
    └── reporte
```

---

## Consideraciones de seguridad

- Contraseñas almacenadas con BCrypt.
- `JWT_SECRET` mediante variable de entorno.
- `DB_PASSWORD` mediante variable de entorno.
- JWT web en cookies `HttpOnly`.
- Access Token de duración limitada.
- Refresh Tokens almacenados en base de datos.
- Rotación de Refresh Tokens.
- Revocación del Refresh Token durante logout.
- Permisos validados en backend.
- CSRF activo en formularios Thymeleaf.
- API con respuestas HTTP apropiadas.

---

## Archivos que no deben incluirse en el repositorio

Se recomienda excluir mediante `.gitignore`:

```text
target/
.idea/
*.iml
*.log
```

No deben incluirse contraseñas reales, secretos JWT ni credenciales personales.

---

## Autor

**Christian Iván Torres Gracián**

Proyecto final del diplomado desarrollado con Spring Boot, Spring Data JPA, Thymeleaf y Spring Security.

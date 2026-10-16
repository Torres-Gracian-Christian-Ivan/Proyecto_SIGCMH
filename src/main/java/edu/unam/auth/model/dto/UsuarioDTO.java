package edu.unam.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Integer id;

    @NotBlank(message = "{usuario.nombre.required}")
    @Size(max = 100, message = "{usuario.nombre.size}")
    private String nombreCompleto;

    @NotBlank(message = "{usuario.email.required}")
    @Email(message = "{usuario.email.invalid}")
    @Size(max = 100, message = "{usuario.email.size}")
    private String email;

    /*
     * Se usa como entrada de contraseña.
     *
     * CREAR:
     * obligatoria, mínimo 8 caracteres.
     *
     * EDITAR:
     * si viene vacía, conserva la contraseña actual.
     *
     * Nunca regresamos el hash BCrypt desde el Mapper.
     */
    private String passwordHash;

    @NotNull(message = "{usuario.perfil.required}")
    private Integer idPerfil;

    // Solo para mostrar información
    private String perfil;

    private Boolean activo;
}
package edu.unam.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PerfilDTO {
    //no usado por ahora
    private Integer id;

    @NotBlank(message = "{perfil.nombre.required}")
    @Size(max = 50, message = "{perfil.nombre.size}")
    private String nombre;

    @Size(max = 150, message = "{perfil.descripcion.size}")
    private String descripcion;

    private Boolean activo;

}
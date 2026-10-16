package edu.unam.system.medico.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MedicoDTO {

    private Integer id;

    @NotBlank(message = "{medico.nombre.required}")
    @Size(max = 100, message = "{medico.nombre.size}")
    private String nombreCompleto;

    @NotBlank(message = "{medico.identificacion.required}")
    @Size(max = 30, message = "{medico.identificacion.size}")
    private String numeroIdentificacion;

    @NotBlank(message = "{medico.telefono.required}")
    @Pattern(
            regexp = "\\d{10}",
            message = "{medico.telefono.pattern}"
    )
    private String telefono;

    @NotBlank(message = "{medico.email.required}")
    @Email(message = "{medico.email.invalid}")
    private String email;

    @NotNull(message = "{medico.especialidad.required}")
    private Integer idEspecialidad;

    /* Solo para mostrar el nombre de la especialidad */
    private String especialidad;

    @NotNull(message = "{medico.consultorio.required}")
    private Integer idConsultorio;

    /* Solo para mostrar el nombre del consultorio */
    private String consultorio;

    private Boolean activo;

}
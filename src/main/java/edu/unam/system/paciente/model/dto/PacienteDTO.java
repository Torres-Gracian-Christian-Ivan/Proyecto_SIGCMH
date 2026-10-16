package edu.unam.system.paciente.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PacienteDTO {

    private Integer idPaciente;

    @NotBlank(message = "{paciente.nombre.required}")
    @Size(max = 100, message = "{paciente.nombre.size}")
    private String nombreCompleto;

    @NotBlank(message = "{paciente.identificacion.required}")
    private String numeroIdentificacion;

    @NotNull(message = "{paciente.fecha.required}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotNull(message = "{paciente.genero.required}")
    private Integer idGenero;

    /* Solo para mostrar el nombre del género */
    private String genero;

    @NotBlank(message = "{paciente.telefono.required}")
    @Pattern(
            regexp = "\\d{10}",
            message = "{paciente.telefono.pattern}"
    )
    private String telefono;

    @NotBlank(message = "{paciente.email.required}")
    @Email(message = "{paciente.email.invalid}")
    private String email;

    @NotBlank(message = "{paciente.direccion.required}")
    private String direccion;

    private Boolean activo;

}
package edu.unam.system.cita.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CitaDTO {

    private Integer id;

    @NotNull(message = "{cita.paciente.required}")
    private Integer idPaciente;

    //Solo para mostrar el nombre del paciente
    private String nombrePaciente;

    @NotNull(message = "{cita.medico.required}")
    private Integer idMedico;

    // Solo para mostr el nombre del médico
    private String nombreMedico;


    @NotNull(message = "{cita.usuario.required}")
    private Integer idUsuarioCreador;

    //Solo para mostrar el nombre del usuario
    private String nombreUsuario;


    @NotNull(message = "{cita.fecha.required}")
    @Future(message = "{cita.fecha.future}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHora;


    @NotNull(message = "{cita.estado.required}")
    private Integer idEstado;



    // Solo para mostrar estado
    private String estado;

    @NotBlank(message = "{cita.motivo.required}")
    @Size(max = 150, message = "{cita.motivo.size}")
    private String motivoConsulta;

    @Size(max = 255, message = "{cita.observaciones.size}")
    private String observaciones;

}
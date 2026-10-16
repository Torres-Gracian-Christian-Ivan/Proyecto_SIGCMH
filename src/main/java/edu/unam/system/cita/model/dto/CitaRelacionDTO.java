package edu.unam.system.cita.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CitaRelacionDTO {

    @NotNull(message = "{cita.usuario.required}")
    private Integer idUsuarioCreador;

    @NotNull(message = "{cita.fecha.required}")
    @Future(message = "{cita.fecha.future}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHora;

    @NotNull(message = "{cita.estado.required}")
    private Integer idEstado;

    @NotBlank(message = "{cita.motivo.required}")
    @Size(max = 150, message = "{cita.motivo.size}")
    private String motivoConsulta;

    @Size(max = 255, message = "{cita.observaciones.size}")
    private String observaciones;
}
package edu.unam.system.horarioatencion.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioAtencionDTO {

    private Integer id;


    // /api/medicos/{idMedico}/horarios
    private Integer idMedico;

    //  mostrar información en la respuesta
    private String nombreMedico;

    @NotNull(message = "El día de la semana es obligatorio")
    @Min(value = 1, message = "El día de la semana debe estar entre 1 y 7")
    @Max(value = 7, message = "El día de la semana debe estar entre 1 y 7")
    private Integer diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Boolean activo;
}
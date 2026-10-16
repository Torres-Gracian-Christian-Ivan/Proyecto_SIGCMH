package edu.unam.system.reporte.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ReporteFiltroDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    private Integer idMedico;

    private Integer idPaciente;

    private Integer idEstado;

    private Integer idEspecialidad;

    private Integer idConsultorio;
}
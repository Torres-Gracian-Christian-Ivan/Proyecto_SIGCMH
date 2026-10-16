package edu.unam.system.reporte.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteCitaDTO {

    private Integer idCita;

    private LocalDateTime fechaHora;

    private String paciente;

    private String medico;

    private String especialidad;

    private String consultorio;

    private String estado;

    private String motivoConsulta;
}
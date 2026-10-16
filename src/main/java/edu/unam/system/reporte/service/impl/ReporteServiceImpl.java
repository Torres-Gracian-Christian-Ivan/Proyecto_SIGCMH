package edu.unam.system.reporte.service.impl;

import edu.unam.system.cita.model.entity.Cita;
import edu.unam.system.reporte.dto.AgendaSemanalDTO;
import edu.unam.system.reporte.dto.ReporteCitaDTO;
import edu.unam.system.reporte.dto.ReporteFiltroDTO;
import edu.unam.system.reporte.repository.ReporteRepository;
import edu.unam.system.reporte.service.ReporteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteServiceImpl(
            ReporteRepository reporteRepository) {

        this.reporteRepository = reporteRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReporteCitaDTO> buscar(
            ReporteFiltroDTO filtros) {

        LocalDateTime fechaInicio = null;
        LocalDateTime fechaFin = null;

        if (filtros.getFecha() != null) {

            fechaInicio =
                    filtros.getFecha()
                            .atStartOfDay();

            fechaFin =
                    filtros.getFecha()
                            .plusDays(1)
                            .atStartOfDay();
        }

        List<Cita> citas =
                reporteRepository.buscarReporte(
                        fechaInicio,
                        fechaFin,
                        filtros.getIdMedico(),
                        filtros.getIdPaciente(),
                        filtros.getIdEstado(),
                        filtros.getIdEspecialidad(),
                        filtros.getIdConsultorio()
                );

        return citas.stream()
                .map(this::convertirDTO)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<AgendaSemanalDTO> buscarAgendaSemanal(
            Integer idConsultorio,
            LocalDate fecha) {

        if (idConsultorio == null || fecha == null) {
            return List.of();
        }

        // Obtener el lunes de la semana seleccionada
        LocalDate lunes =
                fecha.with(DayOfWeek.MONDAY);

        // Lunes a las 00:00
        LocalDateTime inicioSemana =
                lunes.atStartOfDay();

        // Lunes siguiente a las 00:00
        LocalDateTime finSemana =
                lunes.plusDays(7)
                        .atStartOfDay();

        List<Cita> citas =
                reporteRepository.buscarAgendaSemanal(
                        idConsultorio,
                        inicioSemana,
                        finSemana
                );

        return citas.stream()
                .map(this::convertirAgendaDTO)
                .toList();
    }


    private ReporteCitaDTO convertirDTO(
            Cita cita) {

        ReporteCitaDTO dto =
                new ReporteCitaDTO();

        dto.setIdCita(
                cita.getId()
        );

        dto.setFechaHora(
                cita.getFechaHora()
        );

        dto.setPaciente(
                cita.getPaciente()
                        .getNombreCompleto()
        );

        dto.setMedico(
                cita.getMedico()
                        .getNombreCompleto()
        );

        dto.setEspecialidad(
                cita.getMedico()
                        .getEspecialidad()
                        .getNombre()
        );

        dto.setConsultorio(
                String.valueOf(
                        cita.getMedico()
                                .getConsultorio()
                                .getNumero()
                )
        );

        dto.setEstado(
                cita.getEstado()
                        .getNombre()
        );

        dto.setMotivoConsulta(
                cita.getMotivoConsulta()
        );

        return dto;
    }


    private AgendaSemanalDTO convertirAgendaDTO(
            Cita cita) {

        AgendaSemanalDTO dto =
                new AgendaSemanalDTO();

        dto.setIdCita(
                cita.getId()
        );

        dto.setFechaHora(
                cita.getFechaHora()
        );

        dto.setPaciente(
                cita.getPaciente()
                        .getNombreCompleto()
        );

        dto.setMedico(
                cita.getMedico()
                        .getNombreCompleto()
        );

        dto.setEspecialidad(
                cita.getMedico()
                        .getEspecialidad()
                        .getNombre()
        );

        dto.setConsultorio(
                String.valueOf(
                        cita.getMedico()
                                .getConsultorio()
                                .getNumero()
                )
        );

        dto.setEstado(
                cita.getEstado()
                        .getNombre()
        );

        dto.setMotivoConsulta(
                cita.getMotivoConsulta()
        );

        return dto;
    }
}
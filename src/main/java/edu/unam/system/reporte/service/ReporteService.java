package edu.unam.system.reporte.service;

import edu.unam.system.reporte.dto.AgendaSemanalDTO;
import edu.unam.system.reporte.dto.ReporteCitaDTO;
import edu.unam.system.reporte.dto.ReporteFiltroDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {

    List<ReporteCitaDTO> buscar(
            ReporteFiltroDTO filtros
    );

    List<AgendaSemanalDTO> buscarAgendaSemanal(
            Integer idConsultorio,
            LocalDate fecha
    );
}
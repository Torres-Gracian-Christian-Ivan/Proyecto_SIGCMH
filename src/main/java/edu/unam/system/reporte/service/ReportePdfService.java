package edu.unam.system.reporte.service;

import java.time.LocalDate;

public interface ReportePdfService {

    byte[] generarAgendaSemanal(
            Integer idConsultorio,
            LocalDate fecha
    );
}
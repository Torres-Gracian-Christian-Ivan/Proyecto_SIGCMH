package edu.unam.system.reporte.controller;

import edu.unam.system.catalogo.service.ConsultorioService;
import edu.unam.system.catalogo.service.EspecialidadService;
import edu.unam.system.estadocita.service.EstadoCitaService;
import edu.unam.system.medico.service.MedicoService;
import edu.unam.system.paciente.service.PacienteService;
import edu.unam.system.reporte.dto.ReporteFiltroDTO;
import edu.unam.system.reporte.service.ReportePdfService;
import edu.unam.system.reporte.service.ReporteService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;
    private final EstadoCitaService estadoCitaService;
    private final EspecialidadService especialidadService;
    private final ConsultorioService consultorioService;
    private final ReportePdfService reportePdfService;


    public ReporteController(
            ReporteService reporteService,
            ReportePdfService reportePdfService,
            MedicoService medicoService,
            PacienteService pacienteService,
            EstadoCitaService estadoCitaService,
            EspecialidadService especialidadService,
            ConsultorioService consultorioService) {

        this.reporteService = reporteService;
        this.reportePdfService = reportePdfService;
        this.medicoService = medicoService;
        this.pacienteService = pacienteService;
        this.estadoCitaService = estadoCitaService;
        this.especialidadService = especialidadService;
        this.consultorioService = consultorioService;
    }


    // =========================================
    // REPORTE GENERAL
    // =========================================

    @GetMapping
    public String reporteGeneral(
            @ModelAttribute("filtros")
            ReporteFiltroDTO filtros,
            Model model) {

        model.addAttribute(
                "resultados",
                reporteService.buscar(filtros)
        );

        model.addAttribute(
                "medicos",
                medicoService.findAll()
        );

        model.addAttribute(
                "pacientes",
                pacienteService.findAll()
        );

        model.addAttribute(
                "estados",
                estadoCitaService.findAll()
        );

        model.addAttribute(
                "especialidades",
                especialidadService.findAll()
        );

        model.addAttribute(
                "consultorios",
                consultorioService.findAll()
        );

        return "reportes/reporte-general";
    }


    // =========================================
    // AGENDA SEMANAL POR CONSULTORIO
    // =========================================

    @GetMapping("/agenda-semanal")
    public String agendaSemanal(
            @RequestParam(required = false)
            Integer idConsultorio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            Model model) {

        /*
         * Si no se seleccionó una fecha,
         * utilizamos la fecha actual.
         */
        if (fecha == null) {
            fecha = LocalDate.now();
        }


        /*
         * Obtener el lunes y domingo
         * correspondientes a la semana.
         */
        LocalDate inicioSemana =
                fecha.with(DayOfWeek.MONDAY);

        LocalDate finSemana =
                inicioSemana.plusDays(6);


        /*
         * Consultorios para llenar
         * el <select> de la vista.
         */
        model.addAttribute(
                "consultorios",
                consultorioService.findAll()
        );


        /*
         * Valores seleccionados.
         * Nos sirven para conservar los filtros
         * después de realizar la búsqueda.
         */
        model.addAttribute(
                "idConsultorio",
                idConsultorio
        );

        model.addAttribute(
                "fecha",
                fecha
        );


        /*
         * Rango que mostraremos en pantalla.
         */
        model.addAttribute(
                "inicioSemana",
                inicioSemana
        );

        model.addAttribute(
                "finSemana",
                finSemana
        );


        /*
         * Resultados.
         *
         * Si idConsultorio es null,
         * el Service regresará una lista vacía.
         */
        model.addAttribute(
                "agenda",
                reporteService.buscarAgendaSemanal(
                        idConsultorio,
                        fecha
                )
        );


        return "reportes/agenda-semanal";
    }

    @GetMapping("/agenda-semanal/pdf")
    public ResponseEntity<byte[]> descargarAgendaPdf(
            @RequestParam Integer idConsultorio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        byte[] pdf = reportePdfService.generarAgendaSemanal(
                idConsultorio,
                fecha
        );

        LocalDate inicioSemana =
                fecha.with(DayOfWeek.MONDAY);

        String nombreArchivo =
                "agenda-consultorio-"
                        + idConsultorio
                        + "-"
                        + inicioSemana
                        + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + nombreArchivo
                                + "\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
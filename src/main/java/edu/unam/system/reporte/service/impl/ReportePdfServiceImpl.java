package edu.unam.system.reporte.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.catalogo.model.entity.Consultorio;
import edu.unam.system.catalogo.repository.ConsultorioRepository;
import edu.unam.system.reporte.dto.AgendaSemanalDTO;
import edu.unam.system.reporte.service.ReportePdfService;
import edu.unam.system.reporte.service.ReporteService;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportePdfServiceImpl
        implements ReportePdfService {

    private final ReporteService reporteService;
    private final ConsultorioRepository consultorioRepository;


    public ReportePdfServiceImpl(
            ReporteService reporteService,
            ConsultorioRepository consultorioRepository) {

        this.reporteService = reporteService;
        this.consultorioRepository = consultorioRepository;
    }


    @Override
    public byte[] generarAgendaSemanal(
            Integer idConsultorio,
            LocalDate fecha) {

        /*
         * 1. Validar consultorio
         */
        Consultorio consultorio =
                consultorioRepository
                        .findById(idConsultorio)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "consultorio.notfound"
                                )
                        );


        /*
         * 2. Obtener las citas
         */
        List<AgendaSemanalDTO> agenda =
                reporteService.buscarAgendaSemanal(
                        idConsultorio,
                        fecha
                );


        /*
         * 3. Calcular rango semanal
         */
        LocalDate inicioSemana =
                fecha.with(DayOfWeek.MONDAY);

        LocalDate finSemana =
                inicioSemana.plusDays(6);


        /*
         * 4. Preparar salida del PDF
         */
        ByteArrayOutputStream salida =
                new ByteArrayOutputStream();


        /*
         * PDF horizontal
         */
        Document documento =
                new Document(
                        PageSize.A4.rotate(),
                        30,
                        30,
                        30,
                        30
                );


        PdfWriter.getInstance(
                documento,
                salida
        );


        documento.open();


        /*
         * 5. Fuentes
         */
        Font tituloFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        BaseFont.CP1252,                //textos
                        18
                );

        Font subtituloFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        BaseFont.CP1252,
                        11
                );

        Font normalFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA,
                        BaseFont.CP1252,
                        9
                );

        Font encabezadoFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        BaseFont.CP1252,
                        8
                );


        /*
         * 6. Título
         */
        Paragraph titulo =
                new Paragraph(
                        "SIGCMH - Agenda semanal",
                        tituloFont
                );

        titulo.setAlignment(
                Element.ALIGN_CENTER
        );

        titulo.setSpacingAfter(15);

        documento.add(titulo);


        /*
         * 7. Información del reporte
         */
        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );


        Paragraph consultorioTexto =
                new Paragraph(
                        "Consultorio: "
                                + consultorio.getNumero(),
                        subtituloFont
                );

        documento.add(
                consultorioTexto
        );


        Paragraph ubicacionTexto =
                new Paragraph(
                        "Ubicación: "
                                + consultorio.getUbicacion(),
                        normalFont
                );

        documento.add(
                ubicacionTexto
        );


        Paragraph semanaTexto =
                new Paragraph(
                        "Semana: "
                                + inicioSemana.format(formatoFecha)
                                + " al "
                                + finSemana.format(formatoFecha),
                        normalFont
                );

        semanaTexto.setSpacingAfter(15);

        documento.add(
                semanaTexto
        );


        /*
         * 8. Tabla
         *
         * Día
         * Fecha
         * Hora
         * Paciente
         * Médico
         * Especialidad
         * Estado
         * Motivo
         */
        PdfPTable tabla =
                new PdfPTable(8);

        tabla.setWidthPercentage(100);

        tabla.setWidths(
                new float[]{
                        1.3f,
                        1.4f,
                        1.0f,
                        2.3f,
                        2.3f,
                        1.8f,
                        1.4f,
                        2.5f
                }
        );


        /*
         * Encabezados
         */
        agregarEncabezado(
                tabla,
                "Día",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Fecha",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Hora",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Paciente",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Médico",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Especialidad",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Estado",
                encabezadoFont
        );

        agregarEncabezado(
                tabla,
                "Motivo",
                encabezadoFont
        );


        /*
         * 9. Datos
         */
        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern(
                        "HH:mm"
                );


        if (agenda.isEmpty()) {

            PdfPCell celda =
                    new PdfPCell(
                            new Phrase(
                                    "No existen citas registradas "
                                            + "para esta semana.",
                                    normalFont
                            )
                    );

            celda.setColspan(8);

            celda.setHorizontalAlignment(
                    Element.ALIGN_CENTER
            );

            celda.setPadding(10);

            tabla.addCell(celda);

        } else {

            for (AgendaSemanalDTO cita : agenda) {

                agregarCelda(
                        tabla,
                        obtenerDiaSemana(
                                cita.getFechaHora()
                                        .getDayOfWeek()
                        ),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getFechaHora()
                                .toLocalDate()
                                .format(formatoFecha),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getFechaHora()
                                .toLocalTime()
                                .format(formatoHora),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getPaciente(),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getMedico(),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getEspecialidad(),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getEstado(),
                        normalFont
                );


                agregarCelda(
                        tabla,
                        cita.getMotivoConsulta(),
                        normalFont
                );
            }
        }


        documento.add(tabla);


        /*
         * 10. Total de citas
         */
        Paragraph total =
                new Paragraph(
                        "Total de citas: "
                                + agenda.size(),
                        subtituloFont
                );

        total.setSpacingBefore(15);

        documento.add(total);


        /*
         * 11. Cerrar PDF
         */
        documento.close();


        return salida.toByteArray();
    }


    /*
     * Celda de encabezado
     */
    private void agregarEncabezado(
            PdfPTable tabla,
            String texto,
            Font font) {

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto,
                                font
                        )
                );

        celda.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celda.setPadding(6);

        celda.setBackgroundColor(
                new Color(
                        230,
                        230,
                        230
                )
        );

        tabla.addCell(celda);
    }


    /*
     * Celda normal
     */
    private void agregarCelda(
            PdfPTable tabla,
            String texto,
            Font font) {

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto != null
                                        ? texto
                                        : "",
                                font
                        )
                );

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celda.setPadding(5);

        tabla.addCell(celda);
    }


    /*
     * Traducir DayOfWeek
     * para que no aparezca MONDAY,
     * TUESDAY, etc.
     */
    private String obtenerDiaSemana(
            DayOfWeek dia) {

        return switch (dia) {

            case MONDAY ->
                    "Lunes";

            case TUESDAY ->
                    "Martes";

            case WEDNESDAY ->
                    "Miércoles";

            case THURSDAY ->
                    "Jueves";

            case FRIDAY ->
                    "Viernes";

            case SATURDAY ->
                    "Sábado";

            case SUNDAY ->
                    "Domingo";
        };
    }
}
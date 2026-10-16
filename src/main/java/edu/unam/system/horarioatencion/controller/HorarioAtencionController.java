package edu.unam.system.horarioatencion.controller;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.system.horarioatencion.model.dto.HorarioAtencionDTO;
import edu.unam.system.horarioatencion.service.HorarioAtencionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/medicos/{idMedico}/horarios")
public class HorarioAtencionController {

    private final HorarioAtencionService horarioAtencionService;

    public HorarioAtencionController(
            HorarioAtencionService horarioAtencionService) {

        this.horarioAtencionService = horarioAtencionService;
    }


    // LISTAR HORARIOS DEL MÉDICO
    @GetMapping
    public String listar(
            @PathVariable Integer idMedico,
            Model model) {

        List<HorarioAtencionDTO> horarios =
                horarioAtencionService.findByMedico(idMedico);

        model.addAttribute("horarios", horarios);
        model.addAttribute("idMedico", idMedico);

        String nombreMedico =
                horarios.isEmpty()
                        ? "Médico #" + idMedico
                        : horarios.get(0).getNombreMedico();

        model.addAttribute(
                "nombreMedico",
                nombreMedico
        );

        agregarDiasSemana(model);

        return "horarioatencion/lista";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String nuevo(
            @PathVariable Integer idMedico,
            Model model) {

        HorarioAtencionDTO horario =
                new HorarioAtencionDTO();

        horario.setIdMedico(idMedico);
        horario.setActivo(true);

        model.addAttribute(
                "horario",
                horario
        );

        model.addAttribute(
                "idMedico",
                idMedico
        );

        return "horarioatencion/formulario";
    }


    // VER DETALLE
    @GetMapping("/ver/{idHorario}")
    public String ver(
            @PathVariable Integer idMedico,
            @PathVariable Integer idHorario,
            Model model) {

        HorarioAtencionDTO horario =
                horarioAtencionService.findById(
                        idMedico,
                        idHorario
                );

        model.addAttribute(
                "horario",
                horario
        );

        model.addAttribute(
                "idMedico",
                idMedico
        );

        agregarDiasSemana(model);

        return "horarioatencion/detalle";
    }


    // FORMULARIO EDITAR
    @GetMapping("/editar/{idHorario}")
    public String editar(
            @PathVariable Integer idMedico,
            @PathVariable Integer idHorario,
            Model model) {

        HorarioAtencionDTO horario =
                horarioAtencionService.findById(
                        idMedico,
                        idHorario
                );

        model.addAttribute(
                "horario",
                horario
        );

        model.addAttribute(
                "idMedico",
                idMedico
        );

        agregarDiasSemana(model);

        return "horarioatencion/formulario";
    }

    // GUARDAR O ACTUALIZAR
    @PostMapping("/guardar")
    public String guardar(
            @PathVariable Integer idMedico,
            @Valid
            @ModelAttribute("horario")
            HorarioAtencionDTO horario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        horario.setIdMedico(idMedico);

        if (result.hasErrors()) {

            model.addAttribute(
                    "idMedico",
                    idMedico
            );

            return "horarioatencion/formulario";
        }


        try {

            if (horario.getId() == null) {

                horarioAtencionService.save(
                        idMedico,
                        horario
                );

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Horario registrado correctamente"
                );

            } else {

                horarioAtencionService.update(
                        idMedico,
                        horario.getId(),
                        horario
                );

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Horario actualizado correctamente"
                );
            }

        } catch (DuplicateResourceException e) {

            model.addAttribute(
                    "error",
                    "El médico ya tiene un horario registrado que se cruza con el intervalo seleccionado."
            );

            model.addAttribute(
                    "idMedico",
                    idMedico
            );

            return "horarioatencion/formulario";
        }


        return "redirect:/medicos/"
                + idMedico
                + "/horarios";
    }

    // ELIMINAR
    @PostMapping("/eliminar/{idHorario}")
    public String eliminar(
            @PathVariable Integer idMedico,
            @PathVariable Integer idHorario,
            RedirectAttributes redirectAttributes) {

        horarioAtencionService.delete(
                idMedico,
                idHorario
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "Horario eliminado correctamente"
        );

        return "redirect:/medicos/"
                + idMedico
                + "/horarios";
    }


    private void agregarDiasSemana(Model model) {

        model.addAttribute(
                "diasSemana",
                Map.of(
                        1, "Lunes",
                        2, "Martes",
                        3, "Miércoles",
                        4, "Jueves",
                        5, "Viernes",
                        6, "Sábado",
                        7, "Domingo"
                )
        );
    }
}
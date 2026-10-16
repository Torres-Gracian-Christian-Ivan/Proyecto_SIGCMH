package edu.unam.system.paciente.controller;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.paciente.model.dto.PacienteDTO;
import edu.unam.system.catalogo.service.GeneroService;
import edu.unam.system.paciente.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    private final GeneroService generoService;

    public PacienteController(PacienteService pacienteService,
                              GeneroService generoService) {
        this.pacienteService = pacienteService;
        this.generoService = generoService;
    }

    @GetMapping
    public String listarPacientes(Model model){

        model.addAttribute("pacientes",
                pacienteService.findAll());

        return "paciente/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoPaciente(Model model){

        model.addAttribute("paciente", new PacienteDTO());
        model.addAttribute("generos", generoService.findAll());

        return "paciente/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(
            @Valid @ModelAttribute("paciente") PacienteDTO paciente,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if(result.hasErrors()){
            model.addAttribute("generos", generoService.findAll());
            return "paciente/formulario";
        }

        try{

            pacienteService.save(paciente);

        }catch(DuplicateResourceException e){

            model.addAttribute("generos", generoService.findAll());
            model.addAttribute("error", "Ya existe un paciente con ese número de identificación.");

            return "paciente/formulario";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Paciente guardado correctamente.");

        return "redirect:/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editarPaciente(@PathVariable Integer id,
                                 Model model){

        model.addAttribute("paciente",
                pacienteService.findById(id));

        model.addAttribute("generos",
                generoService.findAll());

        return "paciente/formulario";
    }

    @GetMapping("/ver/{id}")
    public String detallePaciente(@PathVariable Integer id,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        try {

            model.addAttribute("paciente",
                    pacienteService.findById(id));

            return "paciente/detalle";

        } catch (ResourceNotFoundException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No fue posible mostrar la información del paciente porque el registro no existe o fue eliminado.");

            return "redirect:/pacientes";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Integer id,
                                   RedirectAttributes redirectAttributes) {
        try {
            pacienteService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Paciente eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No es posible eliminar el paciente porque tiene citas registradas.");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "El paciente no existe o ya fue eliminado.");
        }
        return "redirect:/pacientes";
    }

}
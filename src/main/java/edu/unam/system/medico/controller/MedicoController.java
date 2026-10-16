package edu.unam.system.medico.controller;

import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.medico.model.dto.MedicoDTO;
import edu.unam.system.catalogo.service.ConsultorioService;
import edu.unam.system.catalogo.service.EspecialidadService;
import edu.unam.system.medico.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;
    private final ConsultorioService consultorioService;

    public MedicoController(MedicoService medicoService,
                            EspecialidadService especialidadService,
                            ConsultorioService consultorioService) {

        this.medicoService = medicoService;
        this.especialidadService = especialidadService;
        this.consultorioService = consultorioService;
    }

    // Listar
    @GetMapping
    public String listarMedicos(Model model) {

        model.addAttribute("medicos", medicoService.findAll());

        return "medico/lista";
    }

    // Nuevo
    @GetMapping("/nuevo")
    public String nuevoMedico(Model model) {

        model.addAttribute("medico", new MedicoDTO());
        model.addAttribute("especialidades", especialidadService.findAll());
        model.addAttribute("consultorios", consultorioService.findAll());

        return "medico/formulario";
    }

    // Guardar
    @PostMapping("/guardar")
    public String guardarMedico(
            @Valid @ModelAttribute("medico") MedicoDTO medico,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("especialidades", especialidadService.findAll());
            model.addAttribute("consultorios", consultorioService.findAll());

            return "medico/formulario";
        }

        try {

            medicoService.save(medico);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Médico guardado correctamente.");

        } catch (DuplicateResourceException ex) {

            model.addAttribute("especialidades", especialidadService.findAll());
            model.addAttribute("consultorios", consultorioService.findAll());
            model.addAttribute("error",
                    "Ya existe un médico con ese número de identificación.");

            return "medico/formulario";
        }

        return "redirect:/medicos";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editarMedico(@PathVariable Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        try {

            model.addAttribute("medico", medicoService.findById(id));
            model.addAttribute("especialidades", especialidadService.findAll());
            model.addAttribute("consultorios", consultorioService.findAll());

            return "medico/formulario";

        } catch (ResourceNotFoundException ex) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "El médico que intenta editar no existe o fue eliminado.");

            return "redirect:/medicos";
        }
    }

    // Ver detalle
    @GetMapping("/ver/{id}")
    public String detalleMedico(@PathVariable Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        try {

            model.addAttribute("medico", medicoService.findById(id));

            return "medico/detalle";

        } catch (ResourceNotFoundException ex) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "El médico que intenta consultar no existe o fue eliminado.");

            return "redirect:/medicos";
        }
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            medicoService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Médico eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "No es posible eliminar el médico porque tiene citas registradas.");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "El médico que intenta eliminar no existe o fue eliminado.");
        }
        return "redirect:/medicos";
    }

}
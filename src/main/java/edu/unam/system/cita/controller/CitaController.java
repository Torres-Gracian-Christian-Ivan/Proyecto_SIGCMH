package edu.unam.system.cita.controller;

import edu.unam.auth.service.UsuarioService;
import edu.unam.common.exception.DuplicateResourceException;
import edu.unam.common.exception.ResourceNotFoundException;
import edu.unam.system.cita.model.dto.CitaDTO;
import edu.unam.system.cita.service.CitaService;
import edu.unam.system.paciente.service.PacienteService;
import edu.unam.system.medico.service.MedicoService;
import edu.unam.system.estadocita.service.EstadoCitaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EstadoCitaService estadoCitaService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Lista de citas
     */
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("citas", citaService.findAll());

        return "cita/lista";
    }

    /**
     * Formulario nuevo
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("cita", new CitaDTO());

        cargarCatalogos(model);

        return "cita/formulario";
    }

    /**
     * Guardar
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cita") CitaDTO cita,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {

            cargarCatalogos(model);

            return "cita/formulario";
        }

        try {

            citaService.save(cita);

            return "redirect:/citas";

        } catch (DuplicateResourceException e) {

            model.addAttribute("error", e.getMessage());

            cargarCatalogos(model);

            return "cita/formulario";
        }
    }

    /**
     * Editar
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                         Model model) {

        model.addAttribute("cita",
                citaService.findById(id));

        cargarCatalogos(model);

        return "cita/formulario";
    }

    /**
     * Actualizar
     */
    @PostMapping("/actualizar")
    public String actualizar(@Valid @ModelAttribute("cita") CitaDTO cita,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {

            cargarCatalogos(model);

            return "cita/formulario";
        }

        try {

            citaService.update(cita);

            return "redirect:/citas";

        } catch (DuplicateResourceException e) {

            model.addAttribute("error", e.getMessage());

            cargarCatalogos(model);

            return "cita/formulario";
        }
    }

    /**
     * Ver detalle
     */
    @GetMapping("/ver/{id}")
    public String detalle(@PathVariable Integer id,
                          Model model) {

        model.addAttribute("cita",
                citaService.findById(id));

        return "cita/detalle";
    }

    /**
     * Eliminar
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           Model model) {

        try {

            citaService.delete(id);

        } catch (ResourceNotFoundException e) {

            model.addAttribute("error",
                    e.getMessage());
        }

        return "redirect:/citas";
    }

    /**
     * Cargar catálogos para los select
     */
    private void cargarCatalogos(Model model) {

        model.addAttribute("pacientes",
                pacienteService.findAll());

        model.addAttribute("medicos",
                medicoService.findAll());

        model.addAttribute("estados",
                estadoCitaService.findAll());

        model.addAttribute("usuarios",
                usuarioService.findAll());
    }

}
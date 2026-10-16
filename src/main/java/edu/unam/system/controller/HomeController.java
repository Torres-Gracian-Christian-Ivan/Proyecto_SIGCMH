package edu.unam.system.controller;

import edu.unam.auth.service.UsuarioService;
import edu.unam.system.cita.service.CitaService;
import edu.unam.system.medico.service.MedicoService;
import edu.unam.system.paciente.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final CitaService citaService;
    private final UsuarioService usuarioService;

    public HomeController(
            PacienteService pacienteService,
            MedicoService medicoService,
            CitaService citaService,
            UsuarioService usuarioService) {

        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.citaService = citaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "mensaje",
                "Bienvenido a SIGCMH"
        );

        model.addAttribute(
                "fecha",
                LocalDate.now()
        );

        model.addAttribute(
                "pacientes",
                pacienteService.count()
        );

        model.addAttribute(
                "medicos",
                medicoService.count()
        );

        model.addAttribute(
                "citas",
                citaService.count()
        );

        model.addAttribute(
                "usuarios",
                usuarioService.count()
        );

        return "Principal/principal";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
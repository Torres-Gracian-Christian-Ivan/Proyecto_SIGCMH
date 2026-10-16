package edu.unam.auth.controller;

import edu.unam.auth.model.dto.UsuarioDTO;
import edu.unam.auth.repository.PerfilRepository;
import edu.unam.auth.service.UsuarioService;
import edu.unam.common.exception.DuplicateResourceException;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.util.StringUtils;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final PerfilRepository perfilRepository;


    public UsuarioController(
            UsuarioService usuarioService,
            PerfilRepository perfilRepository) {

        this.usuarioService =
                usuarioService;

        this.perfilRepository =
                perfilRepository;
    }

    @GetMapping
    public String lista(
            Model model) {

        model.addAttribute(
                "usuarios",
                usuarioService.findAll()
        );

        return "usuario/lista";
    }


    @GetMapping("/nuevo")
    public String nuevo(
            Model model) {

        UsuarioDTO usuario =
                new UsuarioDTO();

        usuario.setActivo(true);


        model.addAttribute(
                "usuario",
                usuario
        );


        cargarPerfiles(model);


        return "usuario/formulario";
    }


    @GetMapping("/ver/{id}")
    public String ver(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "usuario",
                usuarioService.findById(id)
        );


        return "usuario/detalle";
    }


    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "usuario",
                usuarioService.findById(id)
        );


        cargarPerfiles(model);


        return "usuario/formulario";
    }



    @PostMapping("/guardar")
    public String guardar(
            @Valid
            @ModelAttribute("usuario")
            UsuarioDTO usuario,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes) {


        // -----------------------------------------
        // valida pass
        // -----------------------------------------

        validarPassword(
                usuario,
                bindingResult
        );


        if (bindingResult.hasErrors()) {

            cargarPerfiles(model);

            return "usuario/formulario";
        }


        try {

            usuarioService.save(
                    usuario
            );

        } catch (DuplicateResourceException e) {

            bindingResult.rejectValue(
                    "email",
                    "usuario.duplicate"
            );


            cargarPerfiles(model);


            return "usuario/formulario";
        }


        if (usuario.getId() == null) {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Usuario creado correctamente."
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Usuario actualizado correctamente."
            );
        }


        return "redirect:/usuarios";
    }


    // =========================================
    // DESACTIVAR
    // =========================================

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        usuarioService.delete(id);


        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Usuario desactivado correctamente."
        );


        return "redirect:/usuarios";
    }


    // =========================================
    // CARGAR PERFILES
    // =========================================

    private void cargarPerfiles(
            Model model) {

        model.addAttribute(
                "perfiles",
                perfilRepository.findAll()
        );
    }


    // =========================================
    // VALIDAR PASSWORD WEB
    // =========================================

    private void validarPassword(
            UsuarioDTO usuario,
            BindingResult bindingResult) {

        String password =
                usuario.getPasswordHash();


        /*
         * Usuario nuevo:
         * contraseña obligatoria.
         */
        if (usuario.getId() == null) {

            if (!StringUtils.hasText(password)) {

                bindingResult.rejectValue(
                        "passwordHash",
                        "usuario.password.required"
                );

                return;
            }
        }


        /*
         * Si se proporcionó una contraseña
         * debe tener entre 8 y 255 caracteres.
         */
        if (StringUtils.hasText(password)
                && (password.length() < 8
                || password.length() > 255)) {

            bindingResult.rejectValue(
                    "passwordHash",
                    "usuario.password.size"
            );
        }
    }
}
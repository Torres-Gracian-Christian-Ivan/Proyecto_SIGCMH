package edu.unam.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PruebaRestController {

    @GetMapping("/publico")
    public String publico() {
        return "Endpoint público accesible sin autenticación";
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "Endpoint protegido correctamente";
    }
}

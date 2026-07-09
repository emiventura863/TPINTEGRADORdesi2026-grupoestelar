package com.example.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entidades.Persona;
import com.example.demo.servicios.PropiedadServicio;

@Controller
public class VistasControlador {

    private final PropiedadServicio propiedadServicio;

    public VistasControlador(PropiedadServicio propiedadServicio) {
        this.propiedadServicio = propiedadServicio;
    }

    @GetMapping("/")
    public String verIndex() {
        return "index";
    }

    @GetMapping("/propiedades-web")
    public String verPropiedades(Model model) {
        var lista = propiedadServicio.listarYFiltrar(null, null, null, null);
        model.addAttribute("listaPropiedades", lista);
        return "propiedades"; 
    }

    @GetMapping("/personasBuscar")
    public String verPersonasBuscar(Model model) {
        // Thymeleaf espera un objeto 'formBean' en el HTML, asi que le envio uno vacio para que no de error
        model.addAttribute("formBean", new Persona()); 
        
        return "personasBuscar"; // devuelve el archivo personasBuscar.html
    }
}
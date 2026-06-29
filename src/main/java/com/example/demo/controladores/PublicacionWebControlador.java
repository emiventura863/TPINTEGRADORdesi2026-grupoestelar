package com.example.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entidades.Publicacion;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.servicios.PublicacionServicio;

@Controller
public class PublicacionWebControlador {

    private final PublicacionServicio publicacionServicio;
    private final PropiedadRepositorio propiedadRepositorio;

    public PublicacionWebControlador(PublicacionServicio publicacionServicio, PropiedadRepositorio propiedadRepositorio) {
        this.publicacionServicio = publicacionServicio;
        this.propiedadRepositorio = propiedadRepositorio;
    }

    @GetMapping("/publicaciones-web")
    public String listar(Model model) {
        model.addAttribute("publicaciones", publicacionServicio.listarPublicaciones());
        return "publicaciones";
    }

    @GetMapping("/publicaciones-web/nueva")
    public String nueva(Model model) {
        model.addAttribute("publicacion", new Publicacion());
        model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
        return "formulario-publicacion";
    }

    @GetMapping("/publicaciones-web/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Publicacion pub = publicacionServicio.buscarPorId(id);
        model.addAttribute("publicacion", pub != null ? pub : new Publicacion());
        model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
        return "formulario-publicacion";
    }

    @PostMapping("/publicaciones-web/guardar")
    public String guardar(Publicacion publicacion) {
        publicacionServicio.guardarPublicacion(publicacion);
        return "redirect:/publicaciones-web";
    }

    @PostMapping("/publicaciones-web/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        publicacionServicio.eliminarPublicacion(id);
        return "redirect:/publicaciones-web";
    }
}

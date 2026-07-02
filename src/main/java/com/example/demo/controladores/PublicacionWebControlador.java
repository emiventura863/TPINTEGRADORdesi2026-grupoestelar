package com.example.demo.controladores;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String listar(@RequestParam(required = false) String propiedad,
                         @RequestParam(required = false) String ciudad,
                         @RequestParam(required = false) String estado,
                         @RequestParam(required = false) Double precioMin,
                         @RequestParam(required = false) Double precioMax,
                         Model model) {
        List<Publicacion> publicaciones = publicacionServicio.listarPublicacionesFiltradas(propiedad, ciudad, estado, precioMin, precioMax);
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
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
    public String guardar(Publicacion publicacion, Model model) {
        try {
            publicacionServicio.guardarPublicacion(publicacion);
            return "redirect:/publicaciones-web";
        } catch (RuntimeException ex) {
            model.addAttribute("errorMensaje", ex.getMessage());
            model.addAttribute("publicacion", publicacion);
            model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
            return "formulario-publicacion";
        }
    }

    @PostMapping("/publicaciones-web/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, Model model) {
        try {
            publicacionServicio.eliminarPublicacion(id);
            return "redirect:/publicaciones-web";
        } catch (RuntimeException ex) {
            model.addAttribute("errorMensaje", ex.getMessage());
            return "redirect:/publicaciones-web";
        }
    }

    @GetMapping("/publicaciones-web/{id}/historial")
    public String historial(@PathVariable Integer id, Model model) {
        Publicacion publicacion = publicacionServicio.buscarPorId(id);
        if (publicacion == null) {
            model.addAttribute("errorMensaje", "No existe la publicación solicitada.");
            return "redirect:/publicaciones-web";
        }
        model.addAttribute("publicacion", publicacion);
        model.addAttribute("historial", publicacionServicio.listarHistorial(id));
        return "historial-publicacion";
    }
}

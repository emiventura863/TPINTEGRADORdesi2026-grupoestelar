package com.example.demo.controladores;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entidades.Publicacion;
import com.example.demo.servicios.PublicacionServicio;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionControlador {

    private final PublicacionServicio publicacionServicio;

    public PublicacionControlador(PublicacionServicio publicacionServicio) {
        this.publicacionServicio = publicacionServicio;
    }

    @GetMapping
    public List<Publicacion> listar() {
        return publicacionServicio.listarPublicaciones();
    }

    @GetMapping("/{id}")
    public Publicacion buscarPorId(@PathVariable Integer id) {
        return publicacionServicio.buscarPorId(id);
    }

    @PostMapping
    public Publicacion guardar(@RequestBody Publicacion publicacion) {
        return publicacionServicio.guardarPublicacion(publicacion);
    }

    @PutMapping("/{id}")
    public Publicacion actualizar(@PathVariable Integer id, @RequestBody Publicacion publicacion) {
        publicacion.setId(id);
        return publicacionServicio.guardarPublicacion(publicacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        publicacionServicio.eliminarPublicacion(id);
    }
}

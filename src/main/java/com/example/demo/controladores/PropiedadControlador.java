package com.example.demo.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entidades.Propiedad;
import com.example.demo.repositorios.PropiedadRepositorio;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadControlador {

	private final PropiedadRepositorio propiedadRepositorio;

    public PropiedadControlador(PropiedadRepositorio propiedadRepositorio) {
        this.propiedadRepositorio = propiedadRepositorio;
    }
        
        @GetMapping
        public List<Propiedad> listar() {
            return propiedadRepositorio.findByEliminadoFalse(); // devuelve solo registros no eliminados
        }

        @PostMapping
        public Propiedad guardar(@RequestBody Propiedad propiedad) {
            return propiedadRepositorio.save(propiedad);
        }
}

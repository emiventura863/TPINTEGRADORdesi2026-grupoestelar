package com.example.demo.servicios;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.entidades.Publicacion;
import com.example.demo.enums.EstadoPublicacion;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.repositorios.PublicacionRepositorio;
import com.example.demo.repositorios.PropiedadRepositorio;
import jakarta.transaction.Transactional;

@Service
public class PublicacionServicio {

    private final PublicacionRepositorio publicacionRepositorio;
    private final PropiedadRepositorio propiedadRepositorio;

    public PublicacionServicio(PublicacionRepositorio publicacionRepositorio, PropiedadRepositorio propiedadRepositorio) {
        this.publicacionRepositorio = publicacionRepositorio;
        this.propiedadRepositorio = propiedadRepositorio;
    }

    public List<Publicacion> listarPublicaciones() {
        return publicacionRepositorio.findByEliminadoFalse();
    }

    @Transactional
    public Publicacion guardarPublicacion(Publicacion publicacion) {
        
        if (publicacion.getPropiedad() == null) {
            throw new RuntimeException("Debe seleccionar una propiedad.");
        }
        if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual() <= 0) {
            throw new RuntimeException("El precio mensual debe ser un número positivo.");
        }

        if (publicacion.getEstadoPublicacion() == EstadoPublicacion.ACTIVA) {
            
            if (publicacion.getPropiedad().getEstado() != EstadoPropiedad.DISPONIBLE) {
                throw new RuntimeException("Solo se pueden activar publicaciones de propiedades disponibles.");
            }
            
            boolean existeActiva;
            if (publicacion.getId() == null) { 
                existeActiva = publicacionRepositorio.existsByPropiedadAndEstadoPublicacionAndEliminadoFalse(publicacion.getPropiedad(), EstadoPublicacion.ACTIVA);
            } else { 
                existeActiva = publicacionRepositorio.existsByPropiedadAndEstadoPublicacionAndIdNotAndEliminadoFalse(publicacion.getPropiedad(), EstadoPublicacion.ACTIVA, publicacion.getId());
            }
            
            if (existeActiva) {
                throw new RuntimeException("Ya existe una publicación activa para esta propiedad.");
            }
        }

        return publicacionRepositorio.save(publicacion);
    }

    public Publicacion buscarPorId(Integer id) {
        return publicacionRepositorio.findById(id).orElse(null);
    }

    public void eliminarPublicacion(Integer id) {
        Publicacion publicacion = buscarPorId(id);

        if (publicacion == null) {
            throw new RuntimeException("No existe la publicación con id: " + id);
        }
        if (publicacion.getEstadoPublicacion() != EstadoPublicacion.ACTIVA) {
            throw new RuntimeException("Solo se pueden eliminar publicaciones activas registradas por error.");
        }

        publicacion.setEliminado(true);
        publicacionRepositorio.save(publicacion);
    }
}
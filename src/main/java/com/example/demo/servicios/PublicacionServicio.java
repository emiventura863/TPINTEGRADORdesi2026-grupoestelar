package com.example.demo.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entidades.EstadoPublicacionHistorial;
import com.example.demo.entidades.Propiedad;
import com.example.demo.entidades.Publicacion;
import com.example.demo.enums.EstadoPublicacion;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.repositorios.EstadoPublicacionHistorialRepositorio;
import com.example.demo.repositorios.PublicacionRepositorio;
import com.example.demo.repositorios.PropiedadRepositorio;

import jakarta.transaction.Transactional;

@Service
public class PublicacionServicio {

    private final PublicacionRepositorio publicacionRepositorio;
    private final PropiedadRepositorio propiedadRepositorio;
    private final EstadoPublicacionHistorialRepositorio historialRepositorio;

    public PublicacionServicio(PublicacionRepositorio publicacionRepositorio,
                               PropiedadRepositorio propiedadRepositorio,
                               EstadoPublicacionHistorialRepositorio historialRepositorio) {
        this.publicacionRepositorio = publicacionRepositorio;
        this.propiedadRepositorio = propiedadRepositorio;
        this.historialRepositorio = historialRepositorio;
    }

    public List<Publicacion> listarPublicaciones() {
        return publicacionRepositorio.findByEliminadoFalse();
    }

    public List<Publicacion> listarPublicacionesFiltradas(String propiedad, String ciudad, String estado, Double precioMin, Double precioMax) {
        List<Publicacion> publicaciones = publicacionRepositorio.findByEliminadoFalse();
        return publicaciones.stream()
                .filter(p -> propiedad == null || propiedad.isBlank() || String.valueOf(p.getPropiedad().getId()).equals(propiedad))
                .filter(p -> ciudad == null || ciudad.isBlank() || p.getPropiedad().getCiudad().equalsIgnoreCase(ciudad))
                .filter(p -> estado == null || estado.isBlank() || p.getEstadoPublicacion().name().equalsIgnoreCase(estado))
                .filter(p -> precioMin == null || p.getPrecioMensual() == null || p.getPrecioMensual() >= precioMin)
                .filter(p -> precioMax == null || p.getPrecioMensual() == null || p.getPrecioMensual() <= precioMax)
                .toList();
    }

    @Transactional
    public Publicacion guardarPublicacion(Publicacion publicacion) {
        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
            throw new RuntimeException("Debe seleccionar una propiedad.");
        }

        Propiedad propiedadPersistida = propiedadRepositorio.findById(publicacion.getPropiedad().getId())
                .orElseThrow(() -> new RuntimeException("La propiedad seleccionada no existe."));

        if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual() <= 0) {
            throw new RuntimeException("El precio mensual debe ser un número positivo.");
        }

        if (publicacion.getFechaPublicacion() == null || publicacion.getFechaPublicacion().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de publicación debe ser una fecha válida.");
        }

        if (publicacion.getCondicionesAlquiler() == null || publicacion.getCondicionesAlquiler().isBlank()) {
            throw new RuntimeException("Las condiciones de alquiler son obligatorias.");
        }

        if (publicacion.getDescripcion() == null || publicacion.getDescripcion().isBlank()) {
            throw new RuntimeException("La descripción es obligatoria.");
        }

        if (publicacion.getEstadoPublicacion() == null) {
            publicacion.setEstadoPublicacion(EstadoPublicacion.ACTIVA);
        }

        boolean esEdicion = publicacion.getId() != null;
        Publicacion existente = null;
        if (esEdicion) {
            existente = publicacionRepositorio.findById(publicacion.getId()).orElse(null);
            if (existente == null) {
                throw new RuntimeException("No existe la publicación solicitada.");
            }

            if (existente.getEstadoPublicacion() == EstadoPublicacion.FINALIZADA &&
                    !existente.getCondicionesAlquiler().equals(publicacion.getCondicionesAlquiler())) {
                throw new RuntimeException("No se pueden modificar las condiciones de alquiler de una publicación finalizada.");
            }

            if (existente.getEstadoPublicacion() == EstadoPublicacion.FINALIZADA &&
                    !existente.getDescripcion().equals(publicacion.getDescripcion())) {
                throw new RuntimeException("No se pueden modificar la descripción de una publicación finalizada.");
            }

            if (publicacion.getPropiedad() != null && !existente.getPropiedad().getId().equals(publicacion.getPropiedad().getId())) {
                throw new RuntimeException("La propiedad asociada no puede modificarse una vez creada la publicación.");
            }

            publicacion.setPropiedad(existente.getPropiedad());
            publicacion.setEliminado(existente.getEliminado());
        }

        if (publicacion.getEstadoPublicacion() == EstadoPublicacion.ACTIVA) {
            if (propiedadPersistida.getEstado() != EstadoPropiedad.DISPONIBLE) {
                throw new RuntimeException("Solo se pueden activar publicaciones de propiedades disponibles.");
            }

            boolean existeActiva;
            if (!esEdicion) {
                existeActiva = publicacionRepositorio.existsByPropiedadAndEstadoPublicacionAndEliminadoFalse(propiedadPersistida, EstadoPublicacion.ACTIVA);
            } else {
                existeActiva = publicacionRepositorio.existsByPropiedadAndEstadoPublicacionAndIdNotAndEliminadoFalse(propiedadPersistida, EstadoPublicacion.ACTIVA, publicacion.getId());
            }

            if (existeActiva) {
                throw new RuntimeException("Ya existe una publicación activa para esta propiedad.");
            }
        }

        Publicacion guardada = publicacionRepositorio.save(publicacion);

        if (esEdicion && existente != null && existente.getEstadoPublicacion() != guardada.getEstadoPublicacion()) {
            EstadoPublicacionHistorial historial = new EstadoPublicacionHistorial();
            historial.setPublicacion(guardada);
            historial.setEstado(guardada.getEstadoPublicacion());
            historialRepositorio.save(historial);
        } else if (!esEdicion) {
            EstadoPublicacionHistorial historial = new EstadoPublicacionHistorial();
            historial.setPublicacion(guardada);
            historial.setEstado(guardada.getEstadoPublicacion());
            historialRepositorio.save(historial);
        }

        return guardada;
    }

    public Publicacion buscarPorId(Integer id) {
        return publicacionRepositorio.findById(id).orElse(null);
    }

    public List<EstadoPublicacionHistorial> listarHistorial(Integer id) {
        Publicacion publicacion = buscarPorId(id);
        if (publicacion == null) {
            throw new RuntimeException("No existe la publicación solicitada.");
        }
        return historialRepositorio.findByPublicacionOrderByFechaCambioAsc(publicacion);
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
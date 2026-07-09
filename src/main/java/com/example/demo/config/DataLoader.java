package com.example.demo.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entidades.EstadoPublicacionHistorial;
import com.example.demo.entidades.Propiedad;
import com.example.demo.entidades.Publicacion;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.EstadoPublicacion;
import com.example.demo.enums.TipoPropiedad;
import com.example.demo.repositorios.EstadoPublicacionHistorialRepositorio;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.repositorios.PublicacionRepositorio;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatosIniciales(PropiedadRepositorio propiedadRepositorio,
                                           PublicacionRepositorio publicacionRepositorio,
                                           EstadoPublicacionHistorialRepositorio historialRepositorio) {
        return args -> {
            if (!propiedadRepositorio.findByEliminadoFalse().isEmpty()) {
                return;
            }

            Propiedad p1 = new Propiedad();
            p1.setDireccion("Av. Siempre Viva 742");
            p1.setCiudad("Córdoba");
            p1.setEstado(EstadoPropiedad.DISPONIBLE);
            p1.setEliminado(false);
            p1.setTipo(TipoPropiedad.CASA);
            p1.setCantidadAmbientes(4);             
            p1.setMetrosCuadrados(120.0);
            propiedadRepositorio.save(p1);

            Propiedad p2 = new Propiedad();
            p2.setDireccion("Belgrano 1234");
            p2.setCiudad("Rosario");
            p2.setEstado(EstadoPropiedad.DISPONIBLE);
            p2.setEliminado(false);
            p2.setTipo(TipoPropiedad.DEPARTAMENTO);
            p2.setCantidadAmbientes(2);             
            p2.setMetrosCuadrados(45.0);
            propiedadRepositorio.save(p2);

            Propiedad p3 = new Propiedad();
            p3.setDireccion("San Martín 456");
            p3.setCiudad("Mendoza");
            p3.setEstado(EstadoPropiedad.RESERVADA);
            p3.setEliminado(false);
            p3.setTipo(TipoPropiedad.LOCAL);  
            p3.setCantidadAmbientes(1);             
            p3.setMetrosCuadrados(75.5);
            propiedadRepositorio.save(p3);

            Propiedad p4 = new Propiedad();
            p4.setDireccion("Mitre 789");
            p4.setCiudad("Córdoba");
            p4.setEstado(EstadoPropiedad.DISPONIBLE);
            p4.setEliminado(false);
            p4.setTipo(TipoPropiedad.DEPARTAMENTO);
            p4.setCantidadAmbientes(2);
            p4.setMetrosCuadrados(60.0);
            propiedadRepositorio.save(p4);

            crearPublicacion(publicacionRepositorio, historialRepositorio, p1, 180000.0, "Depósito de 6 meses.", "Casa con patio y cochera.", LocalDate.now().minusDays(5), EstadoPublicacion.ACTIVA);
            crearPublicacion(publicacionRepositorio, historialRepositorio, p2, 220000.0, "Garantía de 3 meses. No aceptan mascotas.", "Departamento de 2 ambientes con balcón.", LocalDate.now().minusDays(10), EstadoPublicacion.PAUSADA);
            crearPublicacion(publicacionRepositorio, historialRepositorio, p3, 150000.0, "Depósito de 1 mes.", "Local en zona céntrica.", LocalDate.now().minusDays(20), EstadoPublicacion.FINALIZADA);
            crearPublicacion(publicacionRepositorio, historialRepositorio, p4, 260000.0, "Se acepta mascotas y 1 mes de garantía.", "Departamento luminoso con vista al parque.", LocalDate.now().minusDays(2), EstadoPublicacion.ACTIVA);
        };
    }

    private void crearPublicacion(PublicacionRepositorio publicacionRepositorio,
                                  EstadoPublicacionHistorialRepositorio historialRepositorio,
                                  Propiedad propiedad,
                                  Double precio,
                                  String condiciones,
                                  String descripcion,
                                  LocalDate fecha,
                                  EstadoPublicacion estado) {
        Publicacion publicacion = new Publicacion();
        publicacion.setPropiedad(propiedad);
        publicacion.setPrecioMensual(precio);
        publicacion.setCondicionesAlquiler(condiciones);
        publicacion.setDescripcion(descripcion);
        publicacion.setFechaPublicacion(fecha);
        publicacion.setEstadoPublicacion(estado);
        publicacion.setEliminado(false);
        publicacion = publicacionRepositorio.save(publicacion);

        EstadoPublicacionHistorial historial = new EstadoPublicacionHistorial();
        historial.setPublicacion(publicacion);
        historial.setEstado(estado);
        historialRepositorio.save(historial);
    }
}

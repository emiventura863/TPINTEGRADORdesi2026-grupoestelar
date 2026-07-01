package com.example.demo.servicios;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entidades.Propiedad;
import com.example.demo.entidades.Publicacion;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.EstadoPublicacion;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.repositorios.PublicacionRepositorio;

@SpringBootTest
@Transactional
class PublicacionServicioTest {

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private PropiedadRepositorio propiedadRepositorio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Test
    void guardarPublicacion_rechazaEditarCondicionesEnPublicacionFinalizada() {
        Propiedad propiedad = new Propiedad();
        propiedad.setDireccion("Av. Siempre Viva 123");
        propiedad.setCiudad("Córdoba");
        propiedad.setEstado(EstadoPropiedad.DISPONIBLE);
        propiedad = propiedadRepositorio.save(propiedad);

        Publicacion existente = new Publicacion();
        existente.setPropiedad(propiedad);
        existente.setPrecioMensual(15000.0);
        existente.setCondicionesAlquiler("Depósito de 2 meses");
        existente.setDescripcion("Departamento amplio");
        existente.setFechaPublicacion(LocalDate.now());
        existente.setEstadoPublicacion(EstadoPublicacion.FINALIZADA);
        existente = publicacionRepositorio.save(existente);

        Publicacion modificada = new Publicacion();
        modificada.setId(existente.getId());
        modificada.setPropiedad(propiedad);
        modificada.setPrecioMensual(16000.0);
        modificada.setCondicionesAlquiler("Se modifica la condición");
        modificada.setDescripcion("Departamento amplio");
        modificada.setFechaPublicacion(LocalDate.now());
        modificada.setEstadoPublicacion(EstadoPublicacion.FINALIZADA);

        assertThrows(RuntimeException.class, () -> publicacionServicio.guardarPublicacion(modificada));
    }
}

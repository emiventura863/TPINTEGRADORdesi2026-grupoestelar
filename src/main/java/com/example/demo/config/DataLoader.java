package com.example.demo.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entidades.EstadoPublicacionHistorial;
import com.example.demo.entidades.Persona;
import com.example.demo.entidades.Propiedad;
import com.example.demo.entidades.Publicacion;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.EstadoPublicacion;
import com.example.demo.repositorios.EstadoPublicacionHistorialRepositorio;
import com.example.demo.repositorios.PersonaRepositorio;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.repositorios.PublicacionRepositorio;

@Configuration
public class DataLoader {

	@Bean
	CommandLineRunner cargarDatosIniciales(PropiedadRepositorio propiedadRepositorio,
			PublicacionRepositorio publicacionRepositorio, EstadoPublicacionHistorialRepositorio historialRepositorio,
			PersonaRepositorio personaRepositorio) {

		/*
		 * return args -> { if (!propiedadRepositorio.findByEliminadoFalse().isEmpty())
		 * { return; }
		 */

		return args -> {

			// Personas de prueba (propietarios e inquilinos), para poder probar
			// Propiedades y Contratos desde la web sin cargarlas a mano.
			if (personaRepositorio.findByEliminadoFalse().isEmpty()) {
				Persona propietario1 = new Persona();
				propietario1.setNombre("Marcos");
				propietario1.setApellido("Gimenez");
				personaRepositorio.save(propietario1);

				Persona propietario2 = new Persona();
				propietario2.setNombre("Lucía");
				propietario2.setApellido("Fernández");
				personaRepositorio.save(propietario2);

				Persona inquilino1 = new Persona();
				inquilino1.setNombre("Juan");
				inquilino1.setApellido("Pérez");
				personaRepositorio.save(inquilino1);

				Persona inquilino2 = new Persona();
				inquilino2.setNombre("María");
				inquilino2.setApellido("López");
				personaRepositorio.save(inquilino2);
			}

			if (!propiedadRepositorio.findByEliminadoFalse().isEmpty()) {
				return;
			}


			Propiedad p1 = new Propiedad();
			p1.setDireccion("Av. Siempre Viva 742");
			p1.setCiudad("Córdoba");
			p1.setEstado(EstadoPropiedad.DISPONIBLE);
			p1.setEliminado(false);
			propiedadRepositorio.save(p1);

			Propiedad p2 = new Propiedad();
			p2.setDireccion("Belgrano 1234");
			p2.setCiudad("Rosario");
			p2.setEstado(EstadoPropiedad.DISPONIBLE);
			p2.setEliminado(false);
			propiedadRepositorio.save(p2);

			Propiedad p3 = new Propiedad();
			p3.setDireccion("San Martín 456");
			p3.setCiudad("Mendoza");
			p3.setEstado(EstadoPropiedad.RESERVADA);
			p3.setEliminado(false);
			propiedadRepositorio.save(p3);

			Propiedad p4 = new Propiedad();
			p4.setDireccion("Mitre 789");
			p4.setCiudad("Córdoba");
			p4.setEstado(EstadoPropiedad.DISPONIBLE);
			p4.setEliminado(false);
			propiedadRepositorio.save(p4);

			crearPublicacion(publicacionRepositorio, historialRepositorio, p1, 180000.0,
					"Depósito de 2 meses. Se aceptan mascotas.", "Departamento de 2 ambientes con balcón.",
					LocalDate.now().minusDays(5), EstadoPublicacion.ACTIVA);
			crearPublicacion(publicacionRepositorio, historialRepositorio, p2, 220000.0,
					"Garantía de 3 meses. No se aceptan mascotas.", "Casa con patio y cochera.",
					LocalDate.now().minusDays(10), EstadoPublicacion.PAUSADA);
			crearPublicacion(publicacionRepositorio, historialRepositorio, p3, 150000.0, "Depósito de 1 mes.",
					"Departamento compacto en zona céntrica.", LocalDate.now().minusDays(20),
					EstadoPublicacion.FINALIZADA);
			crearPublicacion(publicacionRepositorio, historialRepositorio, p4, 260000.0,
					"Se acepta mascotas y 1 mes de garantía.", "Departamento luminoso con vista al parque.",
					LocalDate.now().minusDays(2), EstadoPublicacion.ACTIVA);
		};
	}

	private void crearPublicacion(PublicacionRepositorio publicacionRepositorio,
			EstadoPublicacionHistorialRepositorio historialRepositorio, Propiedad propiedad, Double precio,
			String condiciones, String descripcion, LocalDate fecha, EstadoPublicacion estado) {
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

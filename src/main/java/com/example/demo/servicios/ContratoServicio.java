package com.example.demo.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entidades.Contrato;
import com.example.demo.enums.EstadoContrato;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.repositorios.ContratoRepositorio;
//import com.sun.tools.javac.util.List;
import com.example.demo.repositorios.PropiedadRepositorio;

import jakarta.transaction.Transactional;

@Service
public class ContratoServicio {

	private final ContratoRepositorio contratoRepositorio;
	private final PropiedadRepositorio propiedadRepositorio;

	public ContratoServicio(ContratoRepositorio contratoRepositorio, PropiedadRepositorio propiedadRepositorio) {
		this.contratoRepositorio = contratoRepositorio;
		this.propiedadRepositorio = propiedadRepositorio;
	}

	// devuelve la lista de contratos que no han sido eliminados, es decir, solo
	// contratos activos o no marcados como eliminados
	public List<Contrato> listarContratos() {
		return contratoRepositorio.findByEliminadoFalse();
	}

	// Filtra el listado de contratos según el dato recibido.
	// Se utiliza un filtro a la vez para simplificar la búsqueda.
	public List<Contrato> filtrarContratos(Integer propiedadId, Integer inquilinoId, EstadoContrato estado,
			LocalDate fechaInicio) {

		// Si se recibe una propiedad, devuelve los contratos de esa propiedad.
		if (propiedadId != null) {
			return contratoRepositorio.findByEliminadoFalseAndPropiedadId(propiedadId);
		}

		// Si se recibe un inquilino, devuelve los contratos de ese inquilino.
		if (inquilinoId != null) {
			return contratoRepositorio.findByEliminadoFalseAndInquilinoId(inquilinoId);
		}

		// Si se recibe un estado, devuelve los contratos con ese estado.
		if (estado != null) {
			return contratoRepositorio.findByEliminadoFalseAndEstado(estado);
		}

		// Si se recibe una fecha de inicio, devuelve los contratos de esa fecha.
		if (fechaInicio != null) {
			return contratoRepositorio.findByEliminadoFalseAndFechaInicio(fechaInicio);
		}

		// Si no se recibe ningún filtro, devuelve todos los contratos no eliminados.
		return contratoRepositorio.findByEliminadoFalse();
	}

	// guarda el contrato en la BD y devuelve el contrato guardado
	// public Contrato guardarContrato(Contrato contrato) {
	// return contratoRepositorio.save(contrato);
	// }

	@Transactional
	public Contrato guardarContrato(Contrato contrato) {

		// Validamos datos obligatorios y valores numéricos según el TP.
		if (contrato.getPropiedad() == null) {
			throw new RuntimeException("Debe seleccionar una propiedad.");
		}

		if (contrato.getInquilino() == null) {
			throw new RuntimeException("Debe seleccionar un inquilino.");
		}

		if (contrato.getFechaInicio() == null) {
			throw new RuntimeException("Debe ingresar una fecha de inicio.");
		}

		// duracion positiva
		if (contrato.getDuracionMeses() == null || contrato.getDuracionMeses() <= 0) {
			throw new RuntimeException("La duración en meses debe ser un número positivo.");
		}

		// importe positivo
		if (contrato.getImporteMensual() == null || contrato.getImporteMensual() <= 0) {
			throw new RuntimeException("El importe mensual debe ser un número positivo.");
		}

		// dia entre 1 y 31
		if (contrato.getDiaVencimiento() == null || contrato.getDiaVencimiento() < 1
				|| contrato.getDiaVencimiento() > 31) {
			throw new RuntimeException("El día de vencimiento debe estar entre 1 y 31.");
		}

		if (contrato.getEstado() == null) {
			contrato.setEstado(EstadoContrato.BORRADOR);
		}

		// Si el contrato tiene un ID significa que ya existe y lo estamos modificando.
		// Si el ID es null, significa que es un contrato nuevo.
		Contrato contratoActual = null;

		if (contrato.getId() != null) {
			contratoActual = buscarPorId(contrato.getId());
		}

		// VALIDACIÓN 1
		// Un contrato FINALIZADO o RESCINDIDO no puede volver a ACTIVO.
		if (contratoActual != null
				&& (contratoActual.getEstado() == EstadoContrato.FINALIZADO
						|| contratoActual.getEstado() == EstadoContrato.RESCINDIDO)
				&& contrato.getEstado() == EstadoContrato.ACTIVO) {

			throw new RuntimeException("No se puede volver un contrato finalizado o rescindido a ACTIVO.");
		}

		// VALIDACIÓN 2
		// Solo ejecutamos esta validación cuando el contrato se está ACTIVANDO.
		//
		// Es decir:
		// - Si es un contrato nuevo y se crea como ACTIVO.
		// - O si un contrato BORRADOR pasa a ACTIVO.
		//
		// Si el contrato ya estaba ACTIVO y solo estamos modificando datos
		// (importe, duración, descripción, etc.), no corresponde volver a validar
		// la disponibilidad de la propiedad.
		boolean seEstaActivando = contrato.getEstado() == EstadoContrato.ACTIVO
				&& (contratoActual == null || contratoActual.getEstado() != EstadoContrato.ACTIVO);

		if (seEstaActivando) {

			// La propiedad debe estar disponible para poder alquilarse.
			if (contrato.getPropiedad().getEstado() != EstadoPropiedad.DISPONIBLE) {

				throw new RuntimeException("No se puede activar el contrato porque la propiedad no está disponible.");
			}

			// Verificamos que la propiedad no tenga ya otro contrato activo.
			// Si estamos modificando un contrato, se excluye el mismo contrato de la
			// búsqueda para que no se detecte a sí mismo como duplicado.
			boolean existeContratoActivo;

			if (contrato.getId() == null) {

				// Contrato nuevo
				existeContratoActivo = contratoRepositorio
						.existsByPropiedadAndEstadoAndEliminadoFalse(contrato.getPropiedad(), EstadoContrato.ACTIVO);

			} else {

				// Contrato existente
				existeContratoActivo = contratoRepositorio.existsByPropiedadAndEstadoAndEliminadoFalseAndIdNot(
						contrato.getPropiedad(), EstadoContrato.ACTIVO, contrato.getId());
			}

			if (existeContratoActivo) {
				throw new RuntimeException("La propiedad ya posee un contrato activo.");
			}

			// Si todas las validaciones fueron correctas, la propiedad pasa a ALQUILADA.
			contrato.getPropiedad().setEstado(EstadoPropiedad.ALQUILADA);

			// Guardamos el nuevo estado de la propiedad.
			propiedadRepositorio.save(contrato.getPropiedad());
		}

		// VALIDACIÓN 3
		// Si un contrato ACTIVO pasa a FINALIZADO o RESCINDIDO, la propiedad vuelve a
		// estar DISPONIBLE.
		if (contratoActual != null && contratoActual.getEstado() == EstadoContrato.ACTIVO
				&& (contrato.getEstado() == EstadoContrato.FINALIZADO
						|| contrato.getEstado() == EstadoContrato.RESCINDIDO)) {

			contrato.getPropiedad().setEstado(EstadoPropiedad.DISPONIBLE);

			propiedadRepositorio.save(contrato.getPropiedad());
		}

		// Si todas las validaciones fueron correctas, finalmente guardamos el contrato en la base de datos.
		return contratoRepositorio.save(contrato);
	}

	// busca un contrato por su id y lo devuelve, si no encuentra devuelve null
	public Contrato buscarPorId(Integer id) {
		return contratoRepositorio.findById(id).orElse(null);
	}

	// elimina el contrato con el id dado sin devolver nada (void), si existe
	public void eliminarContrato(Integer id) {
		Contrato contrato = buscarPorId(id);

		if (contrato == null) {
			throw new RuntimeException("No existe el contrato con id: " + id);
		}

		if (contrato.getEstado() != EstadoContrato.BORRADOR) {
			throw new RuntimeException("Solo se pueden eliminar contratos en estado BORRADOR");
		}

		contrato.setEliminado(true);
		contratoRepositorio.save(contrato);
	}

}

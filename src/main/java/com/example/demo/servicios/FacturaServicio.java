package com.example.demo.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.entidades.Contrato;
import com.example.demo.entidades.Factura;
import com.example.demo.entidades.HistorialEstadoFactura;
import com.example.demo.enums.EstadoContrato;
import com.example.demo.enums.EstadoFactura;
import com.example.demo.repositorios.ContratoRepositorio;
import com.example.demo.repositorios.FacturaRepositorio;

import jakarta.transaction.Transactional;

@Service
public class FacturaServicio {

	private final FacturaRepositorio facturaRepositorio;
	private final ContratoRepositorio contratoRepositorio;

	public FacturaServicio(FacturaRepositorio facturaRepositorio, ContratoRepositorio contratoRepositorio) {
		this.facturaRepositorio = facturaRepositorio;
		this.contratoRepositorio = contratoRepositorio;
	}

	@Transactional
	public List<Factura> listarFacturas(Integer contratoId, Integer propiedadId, Integer inquilinoId,
			EstadoFactura estado, LocalDate fechaVencimientoDesde, LocalDate fechaVencimientoHasta) {

		List<Factura> facturas = facturaRepositorio.findAllNoEliminadasWithRelations();

		return facturas.stream()
				.filter(f -> contratoId == null || (f.getContrato() != null && contratoId.equals(f.getContrato().getId())))
				.filter(f -> propiedadId == null || (f.getContrato() != null && f.getContrato().getPropiedad() != null
						&& propiedadId.equals(f.getContrato().getPropiedad().getId())))
				.filter(f -> inquilinoId == null || (f.getContrato() != null && f.getContrato().getInquilino() != null
						&& inquilinoId.equals(f.getContrato().getInquilino().getId())))
				.filter(f -> estado == null || estado.equals(f.getEstado()))
				.filter(f -> fechaVencimientoDesde == null
						|| (f.getFechaVencimiento() != null && !f.getFechaVencimiento().isBefore(fechaVencimientoDesde)))
				.filter(f -> fechaVencimientoHasta == null
						|| (f.getFechaVencimiento() != null && !f.getFechaVencimiento().isAfter(fechaVencimientoHasta)))
				.collect(Collectors.toList());
	}

	@Transactional
	public Factura buscarPorId(Integer id) {
		return facturaRepositorio.findByIdAndEliminadaFalse(id)
				.orElseThrow(() -> new RuntimeException("No existe la factura con id: " + id));
	}

	@Transactional
	public List<Contrato> obtenerContratosActivosParaAlta() {
		return contratoRepositorio.findByEstadoAndEliminadoFalseWithRelations(EstadoContrato.ACTIVO);
	}

	@Transactional
	public Factura crearFactura(Factura factura) {
		if (factura.getContrato() == null || factura.getContrato().getId() == null) {
			throw new RuntimeException("Debe seleccionar un contrato.");
		}

		Contrato contrato = contratoRepositorio.findById(factura.getContrato().getId())
				.orElseThrow(() -> new RuntimeException("El contrato seleccionado no existe."));

		validarContratoParaAlta(contrato);
		factura.setContrato(contrato);

		validarDatosBasicos(factura);

		if (factura.getEstado() == null) {
			factura.setEstado(EstadoFactura.PENDIENTE);
		}

		aplicarReglasDatosPago(factura);
		factura.setEliminada(false);
		factura.setHistorialEstados(new java.util.ArrayList<>());

		registrarCambioEstado(factura, factura.getEstado());

		return facturaRepositorio.save(factura);
	}

	@Transactional
	public Factura actualizarFactura(Integer id, Factura datos) {
		Factura factura = buscarPorId(id);

		if (factura.getEstado() == EstadoFactura.PAGADA) {
			throw new RuntimeException("No se puede modificar una factura pagada.");
		}
		if (factura.getEstado() == EstadoFactura.ANULADA) {
			throw new RuntimeException("No se puede modificar una factura anulada.");
		}

		EstadoFactura estadoAnterior = factura.getEstado();
		EstadoFactura estadoNuevo = datos.getEstado() != null ? datos.getEstado() : estadoAnterior;

		validarTransicion(estadoAnterior, estadoNuevo);

		factura.setConceptoFacturado(datos.getConceptoFacturado());
		factura.setFechaEmision(datos.getFechaEmision());
		factura.setFechaVencimiento(datos.getFechaVencimiento());
		factura.setImporte(datos.getImporte());
		factura.setEstado(estadoNuevo);
		factura.setFechaPago(datos.getFechaPago());
		factura.setMedio(datos.getMedio());
		factura.setImportePagado(datos.getImportePagado());
		factura.setInteres(datos.getInteres());

		validarDatosBasicos(factura);
		aplicarReglasDatosPago(factura);

		if (!estadoAnterior.equals(estadoNuevo)) {
			registrarCambioEstado(factura, estadoNuevo);
		}

		return facturaRepositorio.save(factura);
	}

	@Transactional
	public void eliminarFactura(Integer id) {
		Factura factura = buscarPorId(id);

		if (factura.getEstado() == EstadoFactura.PAGADA) {
			throw new RuntimeException("No se puede eliminar una factura pagada.");
		}

		factura.setEliminada(true);
		facturaRepositorio.save(factura);
	}

	private void validarContratoParaAlta(Contrato contrato) {
		if (Boolean.TRUE.equals(contrato.getEliminado())) {
			throw new RuntimeException("No se puede crear una factura para un contrato eliminado.");
		}
		if (contrato.getEstado() == EstadoContrato.BORRADOR) {
			throw new RuntimeException("No se puede crear una factura para un contrato en borrador.");
		}
		if (contrato.getEstado() == EstadoContrato.FINALIZADO) {
			throw new RuntimeException("No se puede crear una factura para un contrato finalizado.");
		}
		if (contrato.getEstado() == EstadoContrato.RESCINDIDO) {
			throw new RuntimeException("No se puede crear una factura para un contrato rescindido.");
		}
		if (contrato.getEstado() != EstadoContrato.ACTIVO) {
			throw new RuntimeException("Solo se pueden asociar facturas a contratos activos.");
		}
	}

	private void validarDatosBasicos(Factura factura) {
		if (factura.getConceptoFacturado() == null || factura.getConceptoFacturado().isBlank()) {
			throw new RuntimeException("Debe ingresar el concepto facturado.");
		}
		if (factura.getFechaEmision() == null) {
			throw new RuntimeException("Debe ingresar una fecha de emisión válida.");
		}
		if (factura.getFechaVencimiento() == null) {
			throw new RuntimeException("Debe ingresar una fecha de vencimiento válida.");
		}
		if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
			throw new RuntimeException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.");
		}
		if (factura.getImporte() == null || factura.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("El importe debe ser un número positivo.");
		}
	}

	private void validarTransicion(EstadoFactura actual, EstadoFactura nuevo) {
		if (actual.equals(nuevo)) {
			return;
		}
		if (actual == EstadoFactura.PENDIENTE
				&& (nuevo == EstadoFactura.PAGADA || nuevo == EstadoFactura.VENCIDA || nuevo == EstadoFactura.ANULADA)) {
			return;
		}
		if (actual == EstadoFactura.VENCIDA && nuevo == EstadoFactura.PAGADA) {
			return;
		}
		throw new RuntimeException("Transición de estado no permitida: " + actual + " -> " + nuevo);
	}

	private void aplicarReglasDatosPago(Factura factura) {
		if (factura.getEstado() == EstadoFactura.ANULADA) {
			if (factura.getFechaPago() != null || factura.getMedio() != null || factura.getImportePagado() != null
					|| factura.getInteres() != null) {
				throw new RuntimeException("No se pueden registrar datos de pago en una factura anulada.");
			}
			limpiarDatosPago(factura);
			return;
		}

		if (factura.getEstado() == EstadoFactura.PAGADA) {
			if (factura.getFechaPago() == null) {
				throw new RuntimeException("Debe ingresar la fecha de pago.");
			}
			if (factura.getMedio() == null) {
				throw new RuntimeException("Debe seleccionar el medio de pago.");
			}
			if (factura.getImportePagado() == null || factura.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
				throw new RuntimeException("El importe pagado debe ser un número positivo.");
			}
			if (factura.getInteres() != null && factura.getInteres().compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("El interés no puede ser negativo.");
			}
			return;
		}

		limpiarDatosPago(factura);
	}

	private void limpiarDatosPago(Factura factura) {
		factura.setFechaPago(null);
		factura.setMedio(null);
		factura.setImportePagado(null);
		factura.setInteres(null);
	}

	private void registrarCambioEstado(Factura factura, EstadoFactura estado) {
		HistorialEstadoFactura historial = new HistorialEstadoFactura();
		historial.setEstado(estado);
		historial.setFechaHora(LocalDateTime.now());
		historial.setFactura(factura);
		factura.getHistorialEstados().add(historial);
	}

}

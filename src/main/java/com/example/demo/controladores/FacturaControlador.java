package com.example.demo.controladores;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entidades.Factura;
import com.example.demo.enums.EstadoFactura;
import com.example.demo.enums.MedioPago;
import com.example.demo.repositorios.ContratoRepositorio;
import com.example.demo.repositorios.PersonaRepositorio;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.servicios.FacturaServicio;

import jakarta.validation.Valid;

@Controller
public class FacturaControlador {

	private final FacturaServicio facturaServicio;
	private final ContratoRepositorio contratoRepositorio;
	private final PropiedadRepositorio propiedadRepositorio;
	private final PersonaRepositorio personaRepositorio;

	public FacturaControlador(FacturaServicio facturaServicio, ContratoRepositorio contratoRepositorio,
			PropiedadRepositorio propiedadRepositorio, PersonaRepositorio personaRepositorio) {
		this.facturaServicio = facturaServicio;
		this.contratoRepositorio = contratoRepositorio;
		this.propiedadRepositorio = propiedadRepositorio;
		this.personaRepositorio = personaRepositorio;
	}
@GetMapping("/facturas")
	public String listar(@RequestParam(required = false) Integer contratoId,
			@RequestParam(required = false) Integer propiedadId,
			@RequestParam(required = false) Integer inquilinoId,
			@RequestParam(required = false) EstadoFactura estado,
			@RequestParam(required = false) LocalDate fechaVencimientoDesde,
			@RequestParam(required = false) LocalDate fechaVencimientoHasta,
			Model model) {

		model.addAttribute("facturas",
				facturaServicio.listarFacturas(contratoId, propiedadId, inquilinoId, estado, fechaVencimientoDesde,
						fechaVencimientoHasta));
		model.addAttribute("contratoId", contratoId);
		model.addAttribute("propiedadId", propiedadId);
		model.addAttribute("inquilinoId", inquilinoId);
		model.addAttribute("estado", estado);
		model.addAttribute("fechaVencimientoDesde", fechaVencimientoDesde);
		model.addAttribute("fechaVencimientoHasta", fechaVencimientoHasta);
		model.addAttribute("contratos", contratoRepositorio.findByEliminadoFalse());
		model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
		model.addAttribute("inquilinos", personaRepositorio.findByEliminadoFalse());
		model.addAttribute("estadosFactura", EstadoFactura.values());
		return "facturas/listado";
	}

	@GetMapping("/facturas/nueva")
	public String nueva(Model model) {
		model.addAttribute("factura", new Factura());
		model.addAttribute("esEdicion", false);
		cargarDatosFormulario(model);
		return "facturas/formulario";
	}

	@PostMapping("/facturas")
	public String guardar(@Valid @ModelAttribute("factura") Factura factura, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("esEdicion", false);
			cargarDatosFormulario(model);
			return "facturas/formulario";
		}

		try {
			facturaServicio.crearFactura(factura);
			redirectAttributes.addFlashAttribute("mensajeOk", "Factura creada correctamente.");
			return "redirect:/facturas";
		} catch (RuntimeException e) {
			model.addAttribute("esEdicion", false);
			model.addAttribute("error", e.getMessage());
			cargarDatosFormulario(model);
			return "facturas/formulario";
		}
	}

	@GetMapping("/facturas/{id}/editar")
	public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Factura factura = facturaServicio.buscarPorId(id);
			if (factura.getEstado() == EstadoFactura.PAGADA || factura.getEstado() == EstadoFactura.ANULADA) {
				redirectAttributes.addFlashAttribute("error",
						"No se puede modificar una factura " + factura.getEstado().name().toLowerCase() + ".");
				return "redirect:/facturas";
			}
			model.addAttribute("factura", factura);
			model.addAttribute("esEdicion", true);
			cargarDatosFormulario(model);
			return "facturas/formulario";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/facturas";
		}
	}

	@PostMapping("/facturas/{id}")
	public String actualizar(@PathVariable Integer id, @Valid @ModelAttribute("factura") Factura factura,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("esEdicion", true);
			restaurarContratoEnEdicion(id, factura);
			cargarDatosFormulario(model);
			return "facturas/formulario";
		}

		try {
			facturaServicio.actualizarFactura(id, factura);
			redirectAttributes.addFlashAttribute("mensajeOk", "Factura actualizada correctamente.");
			return "redirect:/facturas";
		} catch (RuntimeException e) {
			model.addAttribute("esEdicion", true);
			model.addAttribute("error", e.getMessage());
			restaurarContratoEnEdicion(id, factura);
			cargarDatosFormulario(model);
			return "facturas/formulario";
		}
	}

	@PostMapping("/facturas/{id}/eliminar")
	public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			facturaServicio.eliminarFactura(id);
			redirectAttributes.addFlashAttribute("mensajeOk", "Factura eliminada correctamente.");
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/facturas";
	}

	private void cargarDatosFormulario(Model model) {
		model.addAttribute("contratosActivos", facturaServicio.obtenerContratosActivosParaAlta());
		model.addAttribute("estadosFactura", EstadoFactura.values());
		model.addAttribute("mediosPago", MedioPago.values());
	}

	private void restaurarContratoEnEdicion(Integer id, Factura factura) {
		Factura existente = facturaServicio.buscarPorId(id);
		factura.setContrato(existente.getContrato());
	}

}


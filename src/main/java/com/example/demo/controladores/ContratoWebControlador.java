package com.example.demo.controladores;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entidades.Contrato;
import com.example.demo.enums.EstadoContrato;
import com.example.demo.repositorios.PersonaRepositorio;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.servicios.ContratoServicio;

@Controller
public class ContratoWebControlador {

    private final ContratoServicio contratoServicio;
    private final PropiedadRepositorio propiedadRepositorio;
    private final PersonaRepositorio personaRepositorio;

    public ContratoWebControlador(
            ContratoServicio contratoServicio,
            PropiedadRepositorio propiedadRepositorio,
            PersonaRepositorio personaRepositorio) {
        this.contratoServicio = contratoServicio;
        this.propiedadRepositorio = propiedadRepositorio;
        this.personaRepositorio = personaRepositorio;
    }

    @GetMapping("/contratos")
    public String listar(
            @RequestParam(required = false) Integer propiedadId,
            @RequestParam(required = false) Integer inquilinoId,
            @RequestParam(required = false) EstadoContrato estado,
            @RequestParam(required = false) LocalDate fechaInicio,
            Model model) {

        model.addAttribute(
                "contratos",
                contratoServicio.filtrarContratos(propiedadId, inquilinoId, estado, fechaInicio));

        model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
        model.addAttribute("inquilinos", personaRepositorio.findByEliminadoFalse());
        model.addAttribute("estadosContrato", EstadoContrato.values());

        model.addAttribute("propiedadId", propiedadId);
        model.addAttribute("inquilinoId", inquilinoId);
        model.addAttribute("estado", estado);
        model.addAttribute("fechaInicio", fechaInicio);

        return "contratos/listado";
    }

    @GetMapping("/contratos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("contrato", new Contrato());
        model.addAttribute("esEdicion", false);
        cargarDatosFormulario(model);
        return "contratos/formulario";
    }

    @GetMapping("/contratos/{id}/editar")
    public String editar(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Contrato contrato = contratoServicio.buscarPorId(id);

        if (contrato == null) {
            redirectAttributes.addFlashAttribute("error", "No existe el contrato solicitado.");
            return "redirect:/contratos";
        }

        model.addAttribute("contrato", contrato);
        model.addAttribute("esEdicion", true);
        cargarDatosFormulario(model);
        return "contratos/formulario";
    }

    @PostMapping("/contratos/guardar")
    public String guardar(
            Contrato contrato,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            contratoServicio.guardarContrato(contrato);
            redirectAttributes.addFlashAttribute("mensajeOk", "Contrato guardado correctamente.");
            return "redirect:/contratos";
        } catch (RuntimeException e) {
            model.addAttribute("contrato", contrato);
            model.addAttribute("esEdicion", contrato.getId() != null);
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model);
            return "contratos/formulario";
        }
    }

    @PostMapping("/contratos/{id}/eliminar")
    public String eliminar(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            contratoServicio.eliminarContrato(id);
            redirectAttributes.addFlashAttribute("mensajeOk", "Contrato eliminado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/contratos";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("propiedades", propiedadRepositorio.findByEliminadoFalse());
        model.addAttribute("inquilinos", personaRepositorio.findByEliminadoFalse());
        model.addAttribute("estadosContrato", EstadoContrato.values());
    }
}
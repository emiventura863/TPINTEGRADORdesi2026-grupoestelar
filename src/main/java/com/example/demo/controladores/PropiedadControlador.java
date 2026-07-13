package com.example.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import com.example.demo.entidades.Persona;
import com.example.demo.entidades.Propiedad;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.TipoPropiedad;
import com.example.demo.servicios.PropiedadServicio;
import com.example.demo.repositorios.PersonaRepositorio;
import com.example.demo.forms.PropiedadFormBean;

@Controller 
@RequestMapping("/propiedades")
public class PropiedadControlador {

    private final PropiedadServicio propiedadServicio;
    private final PersonaRepositorio personaRepositorio;

    public PropiedadControlador(PropiedadServicio propiedadServicio, PersonaRepositorio personaRepositorio) {
        this.propiedadServicio = propiedadServicio;
        this.personaRepositorio = personaRepositorio;
    }

    // LISTAR Y FILTRAR
    @GetMapping
    public String listarOFiltrar(
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) TipoPropiedad tipo,
            @RequestParam(required = false) EstadoPropiedad estado,
            Model model) { 
        
        var lista = propiedadServicio.listarYFiltrar(direccion, ciudad, tipo, estado);
        model.addAttribute("listaPropiedades", lista);
        
        return "propiedades";
    }

    // EDITAR
    @GetMapping("/alta")
    public String mostrarAlta(Model model) {
        model.addAttribute("formBean", new PropiedadFormBean());
        model.addAttribute("allPersonas", personaRepositorio.findByEliminadoFalse());
        return "propiedadEditar";
    }

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) { 
        Propiedad p = propiedadServicio.buscarPorId(id); 
        PropiedadFormBean formBean = new PropiedadFormBean();
        formBean.setId(p.getId());
        formBean.setDireccion(p.getDireccion());
        formBean.setCiudad(p.getCiudad());
        formBean.setTipo(p.getTipo());
        formBean.setCantidadAmbientes(p.getCantidadAmbientes());
        formBean.setMetrosCuadrados(p.getMetrosCuadrados());
        formBean.setDescripcion(p.getDescripcion());
        formBean.setComodidades(p.getComodidades());
        formBean.setEstado(p.getEstado());
        formBean.setIdPropietario(p.getPropietario() != null ? p.getPropietario().getId() : null);
        model.addAttribute("formBean", formBean);
        model.addAttribute("allPersonas", personaRepositorio.findByEliminadoFalse());
        return "propiedadEditar";
    }

    // GUARDAR (POST)
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("formBean") PropiedadFormBean formBean, 
                          BindingResult result, Model model) {

        // si hay errores de validacion (campos vacios, tipo incorrecto)
        if (result.hasErrors()) {
          model.addAttribute("allPersonas", personaRepositorio.findByEliminadoFalse());
          model.addAttribute("error", "Revise los campos obligatorios antes de guardar.");
          return "propiedadEditar";
        }

        try {
            // buscamos la persona (propietario) por el ID que viene en el formulario
            Persona propietario = personaRepositorio.findById(formBean.getIdPropietario())
                                  .orElseThrow(() -> new RuntimeException("No se encontró el propietario seleccionado"));

            // buscamos si tiene ID o una creacion nueva
            Propiedad propiedad = (formBean.getId() != null) ? 
                                  propiedadServicio.buscarPorId(formBean.getId()) : 
                                  new Propiedad();

            // pasan los datos del FormBean a la Entidad
            propiedadServicio.mapearFormAEntidad(formBean, propiedad, propietario);

            // guardamos (se ejecuta la validacion de negocio: contratos activos o duplicados)
            propiedadServicio.guardarPropiedad(propiedad);

            return "redirect:/propiedades";

        } catch (RuntimeException e) {
            // si la validacion de negocio falla (ej: tiene contratos activos), mostramos el error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allPersonas", personaRepositorio.findByEliminadoFalse());
            return "propiedadEditar";
        }
    }

    // ELIMINAR
    @PostMapping("/delete/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            propiedadServicio.eliminarPropiedad(id); // ¡Ahora el método recibe Integer!
            attributes.addFlashAttribute("mensaje", "Propiedad eliminada correctamente.");
        } catch (RuntimeException e) {
            // capturamos el mensaje que definimos en el servicio y lo enviamos a la vista
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/propiedades"; 
    }
}
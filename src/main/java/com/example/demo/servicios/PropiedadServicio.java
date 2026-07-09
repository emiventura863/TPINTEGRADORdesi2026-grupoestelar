package com.example.demo.servicios;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.demo.entidades.Propiedad;
import com.example.demo.entidades.HistorialEstadoPropiedad;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.TipoPropiedad;
import com.example.demo.repositorios.PropiedadRepositorio;
import com.example.demo.repositorios.ContratoRepositorio;
import com.example.demo.enums.EstadoContrato;
import com.example.demo.forms.PropiedadFormBean;
import com.example.demo.entidades.Persona;

@Service
public class PropiedadServicio {

    private final PropiedadRepositorio propiedadRepositorio;
    private final ContratoRepositorio contratoRepositorio; 

    public PropiedadServicio(PropiedadRepositorio propiedadRepositorio, 
                             ContratoRepositorio contratoRepositorio) {
        this.propiedadRepositorio = propiedadRepositorio;
        this.contratoRepositorio = contratoRepositorio;
    }

    public List<Propiedad> listarYFiltrar(String direccion, String ciudad, TipoPropiedad tipo, EstadoPropiedad estado) {
        return propiedadRepositorio.filtrarPropiedades(direccion, ciudad, tipo, estado);
    }

    public Propiedad buscarPorId(Long id) {
        return propiedadRepositorio.findById(id).orElse(null);
    }

    @Transactional
    public Propiedad guardarPropiedad(Propiedad propiedad) {
        if (propiedad.getDireccion() == null || propiedad.getDireccion().trim().isEmpty() ||
            propiedad.getCiudad() == null || propiedad.getCiudad().trim().isEmpty() ||
            propiedad.getTipo() == null || propiedad.getPropietario() == null ||
            propiedad.getDescripcion() == null || propiedad.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("Todos los campos obligatorios deben estar completos, incluyendo la descripción.");
        }

        if (propiedad.getCantidadAmbientes() == null || propiedad.getCantidadAmbientes() <= 0) {
            throw new RuntimeException("La cantidad de ambientes debe ser un número entero positivo.");
        }
        if (propiedad.getMetrosCuadrados() == null || propiedad.getMetrosCuadrados() <= 0) {
            throw new RuntimeException("Los metros cuadrados deben ser un número positivo.");
        }

        boolean esNuevo = (propiedad.getId() == null);
        EstadoPropiedad estadoAnterior = null;

        if (esNuevo) {
            if (propiedadRepositorio.existsByDireccionAndCiudadAndEliminadoFalse(propiedad.getDireccion(), propiedad.getCiudad())) {
                throw new RuntimeException("Ya existe una propiedad registrada con la misma dirección y ciudad.");
            }
            if (propiedad.getEstado() == null) {
                propiedad.setEstado(EstadoPropiedad.DISPONIBLE);
            }
            propiedad.setEliminado(false);
        } else {
            Propiedad propiedadActual = buscarPorId(propiedad.getId());
            if (propiedadActual == null) {
                throw new RuntimeException("No se encontró la propiedad.");
            }
            estadoAnterior = propiedadActual.getEstado();

            if ((propiedad.getEstado() == EstadoPropiedad.DISPONIBLE || 
                propiedad.getEstado() == EstadoPropiedad.INACTIVA) && 
                propiedad.getEstado() != estadoAnterior) {
                if (contratoRepositorio.existsByPropiedadIdAndEstado(propiedad.getId(), EstadoContrato.ACTIVO)) {
                    throw new RuntimeException("No se puede cambiar el estado a " + propiedad.getEstado() + 
                                               " porque la propiedad tiene un contrato activo vigente.");
                }
            }

            if (propiedadRepositorio.existsByDireccionAndEliminadoFalseAndIdNot(propiedad.getDireccion(), propiedad.getId())) {
              throw new RuntimeException("No se puede registrar porque ya existe una propiedad con la misma dirección en la ciudad especificada.");
}
            }

        if (esNuevo || estadoAnterior != propiedad.getEstado()) {
            HistorialEstadoPropiedad nuevoHistorial = new HistorialEstadoPropiedad(
                propiedad.getEstado(),
                LocalDateTime.now(),
                propiedad
            ); // Guarda registro en el historial de estados
            propiedad.getHistorialEstados().add(nuevoHistorial);
        }

        return propiedadRepositorio.save(propiedad);
    }

    @Transactional
    public void eliminarPropiedad(Long id) { 
        Propiedad propiedad = buscarPorId(id);
        
        if (propiedad == null) {
            throw new RuntimeException("No existe la propiedad con el ID: " + id);
        }

        // Verificamos si existe algún contrato activo
        boolean tieneContratos = contratoRepositorio.existsByPropiedadIdAndEstado(id, EstadoContrato.ACTIVO);
        
        // Si existe, lanzamos la excepción para detener el proceso
        if (tieneContratos) {
            throw new RuntimeException("No se puede eliminar la propiedad: tiene contratos activos vigentes.");
        }

        // Si no tiene contratos, procedemos con el borrado lógico
        propiedad.setEliminado(true);
        propiedadRepositorio.save(propiedad);
    }

    // Mapea los datos desde el FormBean hacia la entidad Propiedad
    public void mapearFormAEntidad(PropiedadFormBean formBean, Propiedad propiedad, Persona propietario) {
        propiedad.setDireccion(formBean.getDireccion());
        propiedad.setCiudad(formBean.getCiudad());
        propiedad.setTipo(formBean.getTipo());
        propiedad.setCantidadAmbientes(formBean.getCantidadAmbientes());
        propiedad.setMetrosCuadrados(formBean.getMetrosCuadrados());
        propiedad.setDescripcion(formBean.getDescripcion());
        propiedad.setComodidades(formBean.getComodidades());
        if (formBean.getEstado() != null) {
            propiedad.setEstado(formBean.getEstado());
        }
        propiedad.setPropietario(propietario);
    }
}
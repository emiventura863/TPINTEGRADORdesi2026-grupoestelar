package com.example.demo.entidades;

import jakarta.persistence.*;
import com.example.demo.enums.EstadoContrato;

@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // almacenamos el id de la propiedad para que Spring Data pueda generar existsByPropiedadIdAndEstado(...)
    private Integer propiedadId;

    @Enumerated(EnumType.STRING)
    private EstadoContrato estado;

    public Contrato() {}

    public Contrato(Integer propiedadId, EstadoContrato estado) {
        this.propiedadId = propiedadId;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Integer propiedadId) {
        this.propiedadId = propiedadId;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }
}

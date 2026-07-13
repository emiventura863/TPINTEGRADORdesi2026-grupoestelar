package com.example.demo.forms;

import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.TipoPropiedad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PropiedadFormBean {

    private Integer id;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "El tipo es obligatorio")
    private TipoPropiedad tipo;

    @NotNull(message = "La cantidad de ambientes es obligatoria")
    @Positive(message = "La cantidad de ambientes debe ser positiva")
    private Integer cantidadAmbientes;

    @NotNull(message = "Los metros cuadrados son obligatorios")
    @Positive(message = "Los metros cuadrados deben ser positivos")
    private Double metrosCuadrados;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String comodidades;

    private EstadoPropiedad estado;

    @NotNull(message = "Debe seleccionar un propietario")
    private Integer idPropietario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public TipoPropiedad getTipo() { return tipo; }
    public void setTipo(TipoPropiedad tipo) { this.tipo = tipo; }

    public Integer getCantidadAmbientes() { return cantidadAmbientes; }
    public void setCantidadAmbientes(Integer cantidadAmbientes) { this.cantidadAmbientes = cantidadAmbientes; }

    public Double getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(Double metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getComodidades() { return comodidades; }
    public void setComodidades(String comodidades) { this.comodidades = comodidades; }

    public EstadoPropiedad getEstado() { return estado; }
    public void setEstado(EstadoPropiedad estado) { this.estado = estado; }

    public Integer getIdPropietario() { return idPropietario; }
    public void setIdPropietario(Integer idPropietario) { this.idPropietario = idPropietario; }
}

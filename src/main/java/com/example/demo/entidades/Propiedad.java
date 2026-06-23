package com.example.demo.entidades;

import com.example.demo.enums.EstadoPropiedad;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Propiedad {

	// Genera automáticamente el valor del ID en la base de datos,
	// usando una estrategia de identidad, asegurando que cada contrato reciba un ID
	// único generado por la base
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String direccion;
	private String ciudad;

	// almacena el estado del contrato como un (string) en la base de datos, en vez
	// de un valor numerico
	@Enumerated(EnumType.STRING)
	private EstadoPropiedad estado = EstadoPropiedad.DISPONIBLE;

	// Indica si la propiedad ha sido eliminada logicamente, sin borrarla
	// físicamente de la base de datos
	private Boolean eliminado = false;

	public Propiedad(Integer id, String direccion, String ciudad, EstadoPropiedad estado, Boolean eliminado) {
		super();
		this.id = id;
		this.direccion = direccion;
		this.ciudad = ciudad;
		this.estado = estado;
		this.eliminado = eliminado;
	}

	//	Constructor vacío requerido por JPA/Hibernate para crear objetos automáticamente.
	public Propiedad() {
	}

	// getters y setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public EstadoPropiedad getEstado() {
		return estado;
	}

	public void setEstado(EstadoPropiedad estado) {
		this.estado = estado;
	}

	public Boolean getEliminado() {
		return eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}

}

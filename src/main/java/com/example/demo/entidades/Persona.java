package com.example.demo.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	private String apellido;
    private String dnicuit;
    private String telefono;
	private String email;
	private String domicilio;
	private Boolean eliminado = false;

	public Persona() {
	}

	public Persona(Long id, String nombre, String apellido, String dnicuit, String telefono, String email, String domicilio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dnicuit = dnicuit;
		this.telefono = telefono;
		this.email = email;
		this.domicilio = domicilio;
		this.eliminado = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDnicuit() {
		return dnicuit;
	}

	public void setDnicuit(String dnicuit) {
		this.dnicuit = dnicuit;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Boolean getEliminado() {
		return eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}
}

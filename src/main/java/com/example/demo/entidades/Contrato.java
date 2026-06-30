package com.example.demo.entidades;

import java.time.LocalDate;

import com.example.demo.enums.EstadoContrato;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity /* representa una tabla en la BD */
public class Contrato {

//	clave primaria
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Propiedad propiedad;

	@ManyToOne
	private Persona inquilino;

	private LocalDate fechaInicio;
	private Integer duracionMeses;
	private Double importeMensual;
	private Integer diaVencimiento;
	private String descripcion;

	// guardar como texto BORRADOR, ACTIVO, ETC
	@Enumerated(EnumType.STRING)
	private EstadoContrato estado = EstadoContrato.BORRADOR;

	// Indica si el contrato ha sido eliminado (eliminación lógica). Se usa para
	// marcar el contrato sin borrarlo físicamente de la base de datos
	// en vez de borrarlo de la base, cambia el valor a true y así podés mantener el
	// registro, pero ocultarlo de las operaciones normales.
	// con los getters y setters puedo modificar ese estado cuando lo necesites, sin
	// perder la información.

	private Boolean eliminado = false;

	public Boolean getEliminado() {
		return eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}

	// getters y setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public Double getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(Double importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimiento() {
		return diaVencimiento;
	}

	public void setDiaVencimiento(Integer diaVencimiento) {
		this.diaVencimiento = diaVencimiento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}

	// constructor con los atributos
	public Contrato(Integer id, Propiedad propiedad, Persona inquilino, LocalDate fechaInicio, Integer duracionMeses,
			Double importeMensual, Integer diaVencimiento, String descripcion, EstadoContrato estado) {
		super();
		this.id = id;
		this.propiedad = propiedad;
		this.inquilino = inquilino;
		this.fechaInicio = fechaInicio;
		this.duracionMeses = duracionMeses;
		this.importeMensual = importeMensual;
		this.diaVencimiento = diaVencimiento;
		this.descripcion = descripcion;
		this.estado = estado;
	}

	// constructor vacio publico, Spring Boot (a través de JPA/Hibernate) necesita
	// poder crear objetos de Contrato automáticamente
	// cuando lee datos de la base de datos.
	public Contrato() {
	}

}

package com.example.demo.entidades;

import java.time.LocalDateTime;

import com.example.demo.enums.EstadoFactura;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class HistorialEstadoFactura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private EstadoFactura estado;

	private LocalDateTime fechaHora;

	@ManyToOne
	private Factura factura;

	public HistorialEstadoFactura() {
	}

	public HistorialEstadoFactura(Integer id, EstadoFactura estado, LocalDateTime fechaHora, Factura factura) {
		this.id = id;
		this.estado = estado;
		this.fechaHora = fechaHora;
		this.factura = factura;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

}

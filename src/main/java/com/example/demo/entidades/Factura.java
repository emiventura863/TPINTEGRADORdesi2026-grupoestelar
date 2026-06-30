package com.example.demo.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.enums.EstadoFactura;
import com.example.demo.enums.MedioPago;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Debe ingresar la fecha de emisión.")
	private LocalDate fechaEmision;

	@NotNull(message = "Debe ingresar la fecha de vencimiento.")
	private LocalDate fechaVencimiento;

	@NotNull(message = "Debe ingresar el importe.")
	@Positive(message = "El importe debe ser un número positivo.")
	private BigDecimal importe;

	@Enumerated(EnumType.STRING)
	private EstadoFactura estado = EstadoFactura.PENDIENTE;

	private Boolean eliminada = false;

	private LocalDate fechaPago;

	@Enumerated(EnumType.STRING)
	private MedioPago medio;

	private BigDecimal importePagado;

	private BigDecimal interes;

	@NotBlank(message = "Debe ingresar el concepto facturado.")
	private String conceptoFacturado;

	@ManyToOne
	private Contrato contrato;

	@OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HistorialEstadoFactura> historialEstados = new ArrayList<>();

	public Factura() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public Boolean getEliminada() {
		return eliminada;
	}

	public void setEliminada(Boolean eliminada) {
		this.eliminada = eliminada;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public MedioPago getMedio() {
		return medio;
	}

	public void setMedio(MedioPago medio) {
		this.medio = medio;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(BigDecimal importePagado) {
		this.importePagado = importePagado;
	}

	public BigDecimal getInteres() {
		return interes;
	}

	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}

	public String getConceptoFacturado() {
		return conceptoFacturado;
	}

	public void setConceptoFacturado(String conceptoFacturado) {
		this.conceptoFacturado = conceptoFacturado;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public List<HistorialEstadoFactura> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) {
		this.historialEstados = historialEstados;
	}

}

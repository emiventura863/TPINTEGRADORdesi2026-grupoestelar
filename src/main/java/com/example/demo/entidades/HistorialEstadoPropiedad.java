package com.example.demo.entidades;

import java.time.LocalDateTime;
import com.example.demo.enums.EstadoPropiedad;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "historial_estado_propiedad")
public class HistorialEstadoPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EstadoPropiedad estado;

    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    @JsonIgnore
    private Propiedad propiedad;

    public HistorialEstadoPropiedad() {}

    public HistorialEstadoPropiedad(EstadoPropiedad estado, LocalDateTime fechaHora, Propiedad propiedad) {
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.propiedad = propiedad;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public EstadoPropiedad getEstado() { return estado; }
    public void setEstado(EstadoPropiedad estado) { this.estado = estado; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
}
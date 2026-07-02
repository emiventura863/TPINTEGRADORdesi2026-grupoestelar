package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entidades.EstadoPublicacionHistorial;
import com.example.demo.entidades.Publicacion;

public interface EstadoPublicacionHistorialRepositorio extends JpaRepository<EstadoPublicacionHistorial, Integer> {

    List<EstadoPublicacionHistorial> findByPublicacionOrderByFechaCambioAsc(Publicacion publicacion);
}

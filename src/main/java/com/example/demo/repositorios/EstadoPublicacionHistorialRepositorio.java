package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.EstadoPublicacionHistorial;

@Repository
public interface EstadoPublicacionHistorialRepositorio extends JpaRepository<EstadoPublicacionHistorial, Integer> {
}

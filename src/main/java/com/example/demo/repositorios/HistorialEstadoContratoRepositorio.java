package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entidades.HistorialEstadoContrato;

public interface HistorialEstadoContratoRepositorio extends JpaRepository<HistorialEstadoContrato, Integer> {

}
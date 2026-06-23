package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entidades.Persona;

public interface PersonaRepositorio extends JpaRepository<Persona, Integer> {

	List<Persona> findByEliminadoFalse();
}

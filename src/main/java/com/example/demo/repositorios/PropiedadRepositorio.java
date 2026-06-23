package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entidades.Propiedad;

// entidad y clave primaria de propiedad es id = Integer
public interface PropiedadRepositorio extends JpaRepository<Propiedad, Integer> {

	// propiedades n o eliminadas
	List<Propiedad> findByEliminadoFalse();
}

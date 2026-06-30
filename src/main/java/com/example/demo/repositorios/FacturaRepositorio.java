package com.example.demo.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entidades.Factura;

public interface FacturaRepositorio extends JpaRepository<Factura, Integer> {

	List<Factura> findByEliminadaFalse();

	@Query("SELECT f FROM Factura f "
			+ "JOIN FETCH f.contrato c "
			+ "JOIN FETCH c.propiedad "
			+ "JOIN FETCH c.inquilino "
			+ "WHERE f.id = :id AND f.eliminada = false")
	Optional<Factura> findByIdAndEliminadaFalse(@Param("id") Integer id);

	@Query("SELECT f FROM Factura f "
			+ "JOIN FETCH f.contrato c "
			+ "JOIN FETCH c.propiedad "
			+ "JOIN FETCH c.inquilino "
			+ "WHERE f.eliminada = false")
	List<Factura> findAllNoEliminadasWithRelations();

}

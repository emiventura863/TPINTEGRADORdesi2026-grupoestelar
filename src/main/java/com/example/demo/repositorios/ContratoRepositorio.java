package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entidades.Contrato;
import com.example.demo.entidades.Propiedad;
import com.example.demo.enums.EstadoContrato;

public interface ContratoRepositorio extends JpaRepository<Contrato, Integer> {

	List<Contrato> findByEliminadoFalse();

	boolean existsByPropiedadAndEstadoAndEliminadoFalse(Propiedad propiedad, EstadoContrato estado);

	List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

	@Query("SELECT c FROM Contrato c "
			+ "JOIN FETCH c.propiedad "
			+ "JOIN FETCH c.inquilino "
			+ "WHERE c.estado = :estado AND c.eliminado = false")
	List<Contrato> findByEstadoAndEliminadoFalseWithRelations(@Param("estado") EstadoContrato estado);

}

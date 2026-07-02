package com.example.demo.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entidades.Contrato;
import com.example.demo.entidades.Propiedad;
import com.example.demo.enums.EstadoContrato;

public interface ContratoRepositorio extends JpaRepository<Contrato, Integer> {

	// listado para que no muestre eliminados
	List<Contrato> findByEliminadoFalse();

	// busca si existe un contrato para una propiedad, con un estado específico, y que no esté eliminado
	boolean existsByPropiedadAndEstadoAndEliminadoFalse(Propiedad propiedad, EstadoContrato estado);

	// verifica si existe otro contrato activo para la misma propiedad, excluyendo el contrato actual
	boolean existsByPropiedadAndEstadoAndEliminadoFalseAndIdNot(Propiedad propiedad, EstadoContrato estado, Integer id);

	// busca los contratos no eliminados de una propiedad
	List<Contrato> findByEliminadoFalseAndPropiedadId(Integer propiedadId);

	// busca los contratos no eliminados de un inquilino
	List<Contrato> findByEliminadoFalseAndInquilinoId(Integer inquilinoId);

	// busca los contratos no eliminados según su estado
	List<Contrato> findByEliminadoFalseAndEstado(EstadoContrato estado);

	// busca los contratos no eliminados por fecha de inicio
	List<Contrato> findByEliminadoFalseAndFechaInicio(LocalDate fechaInicio);

	// busca contratos por estado para usar en facturas
	List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

	// busca contratos por estado junto con propiedad e inquilino para facturas
	@Query("SELECT c FROM Contrato c "
			+ "JOIN FETCH c.propiedad "
			+ "JOIN FETCH c.inquilino "
			+ "WHERE c.estado = :estado AND c.eliminado = false")
	List<Contrato> findByEstadoAndEliminadoFalseWithRelations(@Param("estado") EstadoContrato estado);

}
package com.example.demo.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entidades.Contrato;
import com.example.demo.entidades.Propiedad;
import com.example.demo.enums.EstadoContrato;

// contrato: entidad - Integer: tipo de dato -> clave primaria de contrato: ID : integer
// con JpaRepository puedo usar save(); findAll(); findById(); delete(); deleteById(); existsById();
public interface ContratoRepositorio extends JpaRepository<Contrato, Integer> {

	// listado para que no muestre eliminados
	List<Contrato> findByEliminadoFalse();

	// para validar contratos activos
	// Buscá si existe un contrato que pertenezca a una propiedad específica, con un
	// estado específico, y que no esté eliminado
	boolean existsByPropiedadAndEstadoAndEliminadoFalse(Propiedad propiedad, EstadoContrato estado);

	// este metodo es para verificar si existe un contrato activo para la misma propiedad, excluyendo el que se esta modificando
	boolean existsByPropiedadAndEstadoAndEliminadoFalseAndIdNot(Propiedad propiedad, EstadoContrato estado, Integer id);
	
	// metodo para filtrar el listado (propiedad, inquilino, estado de contrato, fecha de inicio)
	
	// Busca los contratos no eliminados de una propiedad.
	List<Contrato> findByEliminadoFalseAndPropiedadId(Integer propiedadId);

	// Busca los contratos no eliminados de un inquilino.
	List<Contrato> findByEliminadoFalseAndInquilinoId(Integer inquilinoId);

	// Busca los contratos no eliminados según su estado.
	List<Contrato> findByEliminadoFalseAndEstado(EstadoContrato estado);

	// Busca los contratos no eliminados por fecha de inicio.
	List<Contrato> findByEliminadoFalseAndFechaInicio(LocalDate fechaInicio);
	
}
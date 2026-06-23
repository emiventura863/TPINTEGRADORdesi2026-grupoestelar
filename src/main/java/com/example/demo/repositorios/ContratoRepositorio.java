package com.example.demo.repositorios;

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
	// Buscá si existe un contrato que pertenezca a una propiedad específica, con un estado específico, y que no esté eliminado
	boolean existsByPropiedadAndEstadoAndEliminadoFalse(Propiedad propiedad, EstadoContrato estado);
	
}

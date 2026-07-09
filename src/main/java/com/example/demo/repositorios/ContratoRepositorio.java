package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entidades.Contrato;
import com.example.demo.enums.EstadoContrato;

@Repository
public interface ContratoRepositorio extends JpaRepository<Contrato, Long> {

    // se genera automaticamente una consulta que verifica si existe un contrato
    // relacionado a una Propiedad (por su ID) y que tenga un estado especifico
    // (por ejemplo ACTIVO)
    boolean existsByPropiedadIdAndEstado(Long propiedadId, EstadoContrato estado);
}
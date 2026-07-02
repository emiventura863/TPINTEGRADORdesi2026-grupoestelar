package com.example.demo.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entidades.Publicacion;
import com.example.demo.entidades.Propiedad;
import com.example.demo.enums.EstadoPublicacion;

public interface PublicacionRepositorio extends JpaRepository<Publicacion, Integer> {
    
    List<Publicacion> findByEliminadoFalse();

    boolean existsByPropiedadAndEstadoPublicacionAndEliminadoFalse(Propiedad propiedad, EstadoPublicacion estado);
    
    boolean existsByPropiedadAndEstadoPublicacionAndIdNotAndEliminadoFalse(Propiedad propiedad, EstadoPublicacion estado, Integer id);
}
package com.example.demo.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entidades.Propiedad;
import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.TipoPropiedad;

public interface PropiedadRepositorio extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalse();

    boolean existsByDireccionAndCiudadAndEliminadoFalse(String direccion, String ciudad);
    boolean existsByDireccionAndEliminadoFalseAndIdNot(String direccion, Long id);

    @Query("SELECT p FROM Propiedad p WHERE p.eliminado = false " +
           "AND (:direccion IS NULL OR p.direccion LIKE %:direccion%) " +
           "AND (:ciudad IS NULL OR p.ciudad LIKE %:ciudad%) " +
           "AND (:tipo IS NULL OR p.tipo = :tipo) " +
           "AND (:estado IS NULL OR p.estado = :estado)")
    List<Propiedad> filtrarPropiedades(
        @Param("direccion") String direccion,
        @Param("ciudad") String ciudad,
        @Param("tipo") TipoPropiedad tipo,
        @Param("estado") EstadoPropiedad estado
    );
}
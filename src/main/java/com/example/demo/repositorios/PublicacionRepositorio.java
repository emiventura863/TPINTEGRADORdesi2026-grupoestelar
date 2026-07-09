package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.Publicacion;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, Integer> {
    List<Publicacion> findByEliminadoFalse();
}

package com.example.demo.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entidades.Persona;
import com.example.demo.repositorios.PersonaRepositorio;

@RestController
@RequestMapping("/api/personas")
public class PersonaControlador {

	private final PersonaRepositorio personaRepositorio;

	public PersonaControlador(PersonaRepositorio personaRepositorio) {
		super();
		this.personaRepositorio = personaRepositorio;
	}

	@GetMapping
	public List<Persona> listar() {
		return personaRepositorio.findByEliminadoFalse(); // devuelve solo las personas que no estan eliminadas 
	}

	@PostMapping
	public Persona guardar(@RequestBody Persona persona) {
		return personaRepositorio.save(persona);
	}

}

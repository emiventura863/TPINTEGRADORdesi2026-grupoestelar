package com.example.demo.controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeControlador {

	@GetMapping("/")
	public String inicio() {
		return "¡Bienvenido a GrupoEstelar! API ejecutándose correctamente en localhost:8081";
	}

	@GetMapping("/health")
	public String health() {
		return "OK";
	}

}

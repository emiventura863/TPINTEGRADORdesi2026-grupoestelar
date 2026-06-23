package com.example.demo.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entidades.Contrato;
import com.example.demo.servicios.ContratoServicio;

@RestController
@RequestMapping("/api/contratos") // defini la ruta base del controlador
public class ContratoControlador {

	private final ContratoServicio contratoServicio;

	public ContratoControlador(ContratoServicio contratoServicio) {
		super();
		this.contratoServicio = contratoServicio;
	}

	// maneja las solicitudes GET a api/contratos y devuelve una lista con todos los
	// contratos almacenados
	@GetMapping // define el tipo de peticion (GET)
	public List<Contrato> listar() {
		return contratoServicio.listarContratos();
	}

	// Este método maneja las solicitudes http de tipo GET a la ruta con un id
	// específico y devuelve el contrato correspondiente si lo encuentra
	@GetMapping("/{id}") // Esta ruta define un endpoint que espera un 'identificador' en la URL y lo
							// utiliza para buscar un contrato específico
	public Contrato buscarPorId(@PathVariable Integer id) {
		return contratoServicio.buscarPorId(id);
	}

	// Recibe un contrato desde el cuerpo de la petición y lo guarda en la base de
	// datos
	@PostMapping // Este método maneja las solicitudes http de tipo POST (envias datos y se
					// genera).
	public Contrato guardarContrato(@RequestBody Contrato contrato) { // @RequestBody Convierte el cuerpo de la
																		// solicitud (JSON) en un objeto Java,
		return contratoServicio.guardarContrato(contrato); // que se usa como entrada en el método
	}

	// recibe un contrato actualizado desde la petición, le asigna el ID de la ruta
	// y lo guarda, actualizando el registro en la base de datos
	@PutMapping("/{id}") // Este método maneja las solicitudes http de tipo PUT (envias datos y se
							// actualiza).
	public Contrato actualizarContrato(@PathVariable Integer id, @RequestBody Contrato contrato) {
		contrato.setId(id);
		return contratoServicio.guardarContrato(contrato);
	}

	// Recibe el ID del contrato desde la ruta y lo elimina de la base de datos
	// @DeleteMapping("/{id}") // maneja las solicitudes HTTP DELETE a la ruta con
	// un ID y elimina el contrato correspondiente
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable Integer id) {
		contratoServicio.eliminarContrato(id);
	}

}

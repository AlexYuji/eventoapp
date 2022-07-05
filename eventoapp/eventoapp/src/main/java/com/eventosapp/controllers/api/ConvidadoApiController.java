package com.eventosapp.controllers.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eventosapp.models.Convidado;
import com.eventosapp.models.Evento;
import com.eventosapp.repository.ConvidadoRepository;
import com.eventosapp.repository.EventoRepository;

@RestController
@RequestMapping("/api/convidados")
public class ConvidadoApiController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Iterable<Convidado>> listaConvidado(@PathVariable("codigo") Long codigo) {
		Evento evento = er.findByCodigo(codigo);
		Iterable<Convidado> convidado = cr.findByEvento(evento);
		if(convidado == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(convidado);
	}
	
	@PostMapping("/{codigo}")
	public ResponseEntity<Convidado> cadastroConvidado(@PathVariable("codigo") Long codigo, @RequestBody Convidado convidado) {	
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		convidado = cr.saveAndFlush(convidado);
		return ResponseEntity.ok(convidado);
	}	
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Convidado> deletarConvidadoEvento(@PathVariable("codigo") Long codigo) {
		if(cr.existsById(codigo)) {
			cr.deleteById(codigo);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();	 		
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
	}
	
	@PutMapping("/{codigo}")
	@ResponseBody
	public ResponseEntity atualizarConvidado(@PathVariable("codigo") Long codigo, @RequestBody Convidado convidado) {
		return cr.findById(codigo).map(convidadoExistente -> {
			convidado.setId(convidadoExistente.getId());
			convidado.setEvento(convidadoExistente.getEvento());
			cr.saveAndFlush(convidado);
			return ResponseEntity.noContent().build();
		}).orElseGet( () -> ResponseEntity.notFound().build());
	}
	
}

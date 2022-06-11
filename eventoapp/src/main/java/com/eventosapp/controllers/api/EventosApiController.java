package com.eventosapp.controllers.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventosapp.models.Convidado;
import com.eventosapp.models.Evento;
import com.eventosapp.repository.ConvidadoRepository;
import com.eventosapp.repository.EventoRepository;

@RestController
@RequestMapping("/api/eventos")
public class EventosApiController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@GetMapping()
	public ResponseEntity<List<Evento>> form() {
		return ResponseEntity.ok(er.findAll());
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Optional<Evento>> listaEvento(@PathVariable("codigo") Long codigo) {
		Optional<Evento> evento = er.findById(codigo);
		if(evento.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(evento);
	}
	
	@GetMapping("/convidado/{codigo}")
	public ResponseEntity<Optional<Convidado>> listaConvidado(@PathVariable("codigo") Long codigo) {
		Optional<Convidado> convidado = cr.findById(codigo);
		if(convidado.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(convidado);
	}
	
	@PostMapping("/")
	public ResponseEntity<Evento> cadastroEvento(@RequestBody Evento evento) {	
		er.save(evento);
		return ResponseEntity.ok(evento);
	}
	
	@PostMapping("/convidado/{codigo}")
	public ResponseEntity<Convidado> cadastroConvidado(@PathVariable("codigo") long codigo, @RequestBody Convidado convidado) {	
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		convidado = cr.saveAndFlush(convidado);
		return ResponseEntity.ok(convidado);
	}	
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Evento> deletarEvento(@PathVariable("codigo") Long codigo) {
			
		if(er.existsById(codigo)) {
			List<Long> convidadosId = er.findAllConvidadosIdByEventoId(codigo);
			for (Long convidado : convidadosId) {
				cr.deleteById(convidado);
			}
			er.deleteById(codigo);				
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();			
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/convidado/{codigo}")
	public ResponseEntity<Convidado> deletarConvidadoEvento(@PathVariable("codigo") Long codigo) {
		if(cr.existsById(codigo)) {
			cr.deleteById(codigo);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();	 		
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
	}
	
	
	
}

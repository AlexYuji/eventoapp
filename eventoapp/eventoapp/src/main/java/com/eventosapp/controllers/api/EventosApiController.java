package com.eventosapp.controllers.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
	public ResponseEntity<?> form(Pageable pageable) {
		return ResponseEntity.ok(er.findAll(pageable));
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Optional<Evento>> listaEvento(@PathVariable("codigo") Long codigo) {
		Optional<Evento> evento = er.findById(codigo);
		if(evento.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(evento);
	}
	
	@PostMapping("/")
	public ResponseEntity<Evento> cadastroEvento(@RequestBody Evento evento) {	
		er.save(evento);
		return ResponseEntity.ok(evento);
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
	
	@PutMapping("/{codigo}")
	@ResponseBody
	public ResponseEntity atualizarEvento(@PathVariable("codigo") Long codigo, @RequestBody Evento evento) {
		return er.findById(codigo).map( eventoExistente -> {
			evento.setCodigo(eventoExistente.getCodigo());
			er.save(evento);
			return ResponseEntity.noContent().build();
		}).orElseGet( () -> ResponseEntity.notFound().build() );
	}
		
	
}

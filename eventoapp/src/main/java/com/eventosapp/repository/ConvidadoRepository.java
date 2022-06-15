package com.eventosapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventosapp.models.Convidado;
import com.eventosapp.models.Evento;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long>{
	
	Iterable<Convidado> findByEvento(Evento evento);
	
	Convidado findByRg(String rg);	
	
	Convidado findById(long id);
	
}

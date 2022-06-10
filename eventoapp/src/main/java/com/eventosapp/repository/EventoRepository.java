package com.eventosapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventosapp.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{
	
	Evento findByCodigo(long codigo);
	
}

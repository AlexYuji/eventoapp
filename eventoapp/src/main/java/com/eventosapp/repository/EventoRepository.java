package com.eventosapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eventosapp.models.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{
	
	Evento findByCodigo(long codigo);
	
	@Query(nativeQuery = true, value="SELECT id from convidado as c inner join evento as e on c.evento_codigo = e.codigo where e.codigo = :codigo ")
	List<Long> findAllConvidadosIdByEventoId(Long codigo);
	
}

package com.eventosapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Convidado {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	@NotNull
	private String rg;
	
	@NotEmpty
	@NotNull
	private String nomeConvidado;
	
	@ManyToOne
	private Evento evento;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getNomeConvidado() {
		return nomeConvidado;
	}
	public void setNomeConvidado(String nomeConvidado) {
		this.nomeConvidado = nomeConvidado;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	
	public void fromConvidado(Convidado convidado) {
		this.nomeConvidado = convidado.getNomeConvidado();
		this.rg = convidado.getRg();
		this.evento = convidado.getEvento();
	}
	
	public Convidado toConvidado(Convidado convidado) {
		convidado.setNomeConvidado(nomeConvidado);
		convidado.setRg(rg);
		return convidado;
	}
	
}
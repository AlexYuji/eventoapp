package com.eventosapp.controllers;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventosapp.models.Convidado;
import com.eventosapp.models.Evento;
import com.eventosapp.repository.ConvidadoRepository;
import com.eventosapp.repository.EventoRepository;

@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Ops... Algo deu errado! Verifique os Campos!");
			return "redirect:/cadastrarEvento";
		}
		
		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		return "redirect:/cadastrarEvento";
	}

	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("cadastraEvento");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		return mv;
	}	
	
	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		
		if(er.existsById(codigo)) {
			List<Long> convidadosIds = er.findAllConvidadosIdByEventoId(codigo);
			for (Long convidadoId : convidadosIds) {
				cr.deleteById(convidadoId);
			}
			er.deleteById(codigo);
		}
		
		return "redirect:/eventos";
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String cadastrarConvidado(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {	
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Ops... Algo deu errado! Verifique os Campos!");
			return "redirect:/{codigo}";
		}
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		attributes.addFlashAttribute("mensagem", "Convidado cadastrado com sucesso!");
		cr.save(convidado);
		return "redirect:/{codigo}";
	}	
	
	@RequestMapping("/deletarConvidado") 
	public String deletarConvidado(long id) {
		Convidado convidado = cr.findById(id);
		cr.delete(convidado);
		
		Evento evento = convidado.getEvento();	
		long codigoLong = evento.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;			
	}
	
	@GetMapping("/convidados/{codigo}/edit")
	public ModelAndView edit(@PathVariable("codigo") Long codigo, Convidado convidado) {
		Optional<Convidado> optional = cr.findById(codigo);
		
		if(optional.isPresent()) {
			Convidado optionalConvidado = optional.get();
			convidado.fromConvidado(optionalConvidado);
			ModelAndView mv = new ModelAndView("convidado/edit");	
			mv.addObject("convidadoId", optionalConvidado.getId());
			mv.addObject("convidado", optionalConvidado);
			return mv;
		}
		ModelAndView mv = new ModelAndView("/{codigo}");
		return mv;
	}
	
	@PostMapping("/convidados/{codigo}")
    public ModelAndView update(@PathVariable("codigo") Long codigoConvidado, @Valid Convidado newConvidado, BindingResult result, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        if(result.hasErrors() || codigoConvidado == null) return modelAndViewError(modelAndView, codigoConvidado, attributes);
        
        Optional<Convidado> convidadoOptional = cr.findById(codigoConvidado);

        if(convidadoOptional.isEmpty()) return modelAndViewError(modelAndView, codigoConvidado, attributes);

        Convidado convidado = convidadoOptional.get();

        cr.save(newConvidado.toConvidado(convidado));
        modelAndView.setViewName("redirect:/" + convidado.getEvento().getCodigo());

        return modelAndView;
    }

    private ModelAndView modelAndViewError(ModelAndView modelAndView, Long codigoConvidado, RedirectAttributes attributes) {
        modelAndView.setViewName("redirect:/convidados/" + codigoConvidado + "/edit");
        attributes.addFlashAttribute("mensagem", "Ops... Algo deu errado! Verifique os Campos!");
        return modelAndView;
    }
			
}
	


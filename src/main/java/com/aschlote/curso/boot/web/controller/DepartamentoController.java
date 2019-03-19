package com.aschlote.curso.boot.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aschlote.curso.boot.domain.Departamento;

import com.aschlote.curso.boot.service.*;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {
	
	@Autowired
	private DepartamentoService service;
	
	@GetMapping("cadastrar")
	public String cadastrar(Departamento departamento) {
		return "/departamento/cadastro";
	}
	
	@GetMapping("listar")
	public String listar(ModelMap model) {
		model.addAttribute("departamentos", service.buscarTodos());
		return "/departamento/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Departamento departamento, BindingResult result, RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "departamento/cadastro";
		}
		
		this.service.salvar(departamento);
		attr.addFlashAttribute("success","Departamento inserido com sucesso");
		return "redirect:/departamentos/cadastrar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("departamento", service.buscarPorId(id));
		return "/departamento/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Departamento departamento, BindingResult result, RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			return "departamento/cadastro";
		}
		
		this.service.editar(departamento);
		attr.addFlashAttribute("success","Departamento editado com sucesso");
		return "redirect:/departamentos/cadastrar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		
		if	(service.departamentoTemCargos(id)) {
			model.addAttribute("fail","Departamento não removido. Possui cargo(s) vinculados");
		} else {
			service.excluir(id);
			model.addAttribute("success","Departamento removido com sucesso");
		}
		
		return listar(model);
	}

}
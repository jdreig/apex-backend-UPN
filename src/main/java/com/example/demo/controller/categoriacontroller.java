package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.categoria;
import com.example.demo.repository.categoryRepository;


@RestController
@RequestMapping("/api")
public class categoriacontroller {
	@Autowired
	private categoryRepository categoryRep;

	@GetMapping("/categorias")
	public List<categoria> listarcategorias() {
	    return categoryRep.findAll();
	}
}

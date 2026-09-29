package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoryRepository;

@RestController
@RequestMapping("/api")
public class CategoriaController {

    private final CategoryRepository categoryRep;

    // Inyección por constructor requerida por SonarQube
    public CategoriaController(CategoryRepository categoryRep) {
        this.categoryRep = categoryRep;
    }

    @GetMapping("/categorias")
    public List<Categoria> listarcategorias() {
        return categoryRep.findAll();
    }
}
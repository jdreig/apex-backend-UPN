package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.categoria;
import com.example.demo.repository.categoryRepository;

@RestController
@RequestMapping("/api")
public class categoriacontroller {

    private final categoryRepository categoryRep;

    // Inyección por constructor requerida por SonarQube
    public categoriacontroller(categoryRepository categoryRep) {
        this.categoryRep = categoryRep;
    }

    @GetMapping("/categorias")
    public List<categoria> listarcategorias() {
        return categoryRep.findAll();
    }
}
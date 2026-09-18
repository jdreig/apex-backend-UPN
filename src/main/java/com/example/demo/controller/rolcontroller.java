package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.rol;
import com.example.demo.repository.rolRepository;

@RestController
@RequestMapping("/api")

public class rolcontroller {

    @Autowired
    private rolRepository rolRepo;

    // GET listar roles
    @GetMapping("/rol")
    public List<rol> listarRoles() {
        return rolRepo.findAll();
    }
}

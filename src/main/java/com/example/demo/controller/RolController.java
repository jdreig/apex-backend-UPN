package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;

@RestController
@RequestMapping("/api")
public class RolController {

    // 1. Declaramos el repositorio como final
    private final RolRepository rolRepo;

    // 2. Inyección por constructor (Sin @Autowired, ya no es necesario)
    public RolController(RolRepository rolRepo) {
        this.rolRepo = rolRepo;
    }

    // GET listar roles
    @GetMapping("/rol")
    public List<Rol> listarRoles() {
        return rolRepo.findAll();
    }
}
package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.example.demo.model.empresa;
import com.example.demo.repository.empresaRepository;

@RestController
@RequestMapping("/api")
public class empresacontroller {

    @Autowired
    private empresaRepository empresaResp;

    // GET listar todas las empresas
    @GetMapping("/empresa")
    public List<empresa> listarEmpresas() {
        return empresaResp.findAll();
    }
    
    // GET: por id
    @GetMapping("/empresa/{id}")
    public empresa obtenerEmpresa(@PathVariable("id") Long id) {
        return empresaResp.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));
    }

    
    // POST crear nueva empresa
    @PostMapping("/empresa")
    public empresa crearEmpresa(@RequestBody empresa nuevaEmpresa) {
        return empresaResp.save(nuevaEmpresa);
    }
    

    // PUT actualizar empresa
    @PutMapping("/empresa/{id}")
    public empresa actualizarEmpresa(@PathVariable("id") Long id, @RequestBody empresa empresaActualizada) {
        empresa empresaExistente = empresaResp.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        empresaExistente.setRuc(empresaActualizada.getRuc());
        empresaExistente.setRazonsocial(empresaActualizada.getRazonsocial());
        empresaExistente.setDireccion(empresaActualizada.getDireccion());
        empresaExistente.setCorreo(empresaActualizada.getCorreo());
        empresaExistente.setTelefono(empresaActualizada.getTelefono());

        return empresaResp.save(empresaExistente);
    }

}

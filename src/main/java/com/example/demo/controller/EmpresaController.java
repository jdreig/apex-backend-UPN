package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.example.demo.model.Empresa;
import com.example.demo.dto.EmpresaDto; // Importa tu DTO
import com.example.demo.repository.EmpresaRepository;

@RestController
@RequestMapping("/api")
public class EmpresaController {

    private final EmpresaRepository empresaResp;

    public EmpresaController(EmpresaRepository empresaResp) {
        this.empresaResp = empresaResp;
    }

    @GetMapping("/empresa")
    public List<Empresa> listarEmpresas() {
        return empresaResp.findAll();
    }

    @GetMapping("/empresa/{id}")
    public Empresa obtenerEmpresa(@PathVariable("id") Long id) {
        return empresaResp.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));
    }

    // POST usando DTO
    @PostMapping("/empresa")
    public Empresa crearEmpresa(@RequestBody EmpresaDto empresaDto) {
        
        // Creamos una nueva entidad a partir del DTO
        Empresa nuevaEmpresa = new Empresa();
        nuevaEmpresa.setRuc(empresaDto.getRuc());
        nuevaEmpresa.setRazonsocial(empresaDto.getRazonsocial());
        nuevaEmpresa.setDireccion(empresaDto.getDireccion());
        nuevaEmpresa.setCorreo(empresaDto.getCorreo());
        nuevaEmpresa.setTelefono(empresaDto.getTelefono());

        if (!nuevaEmpresa.esRucValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC ingresado es invalido o no cumple con el formato SUNAT");
        }

        return empresaResp.save(nuevaEmpresa);
    }

    // PUT usando DTO
    @PutMapping("/empresa/{id}")
    public Empresa actualizarEmpresa(@PathVariable("id") Long id, @RequestBody EmpresaDto empresaDto) {

        Empresa empresaExistente = empresaResp.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        // Actualizamos los campos usando los valores del DTO
        empresaExistente.setRuc(empresaDto.getRuc());
        empresaExistente.setRazonsocial(empresaDto.getRazonsocial());
        empresaExistente.setDireccion(empresaDto.getDireccion());
        empresaExistente.setCorreo(empresaDto.getCorreo());
        empresaExistente.setTelefono(empresaDto.getTelefono());

        if (!empresaExistente.esRucValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC ingresado es invalido o no cumple con el formato SUNAT");
        }

        return empresaResp.save(empresaExistente);
    }
}
package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "cliente")
public class cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcliente;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private usuario usuario;
    
    // Relación con Empresa
    @ManyToOne
    @JoinColumn(name = "idempresa", nullable = false)
    private empresa empresa;

    // Getters y Setters
    public Long getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Long idcliente) {
        this.idcliente = idcliente;
    }

    public usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(usuario usuario) {
        this.usuario = usuario;
    }

    public empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(empresa empresa) {
        this.empresa = empresa;
    }
}

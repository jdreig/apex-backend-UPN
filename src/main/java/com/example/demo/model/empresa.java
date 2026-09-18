package com.example.demo.model;

import jakarta.persistence.Entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa")
public class empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idempresa;

    private String ruc;
    private String razonsocial;
    private String direccion;
    private String correo;
    private String telefono;

    // Getters
    public Long getIdempresa() {
        return idempresa;
    }

    public String getRuc() {
        return ruc;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    // Setters
    public void setIdempresa(Long idempresa) {
        this.idempresa = idempresa;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

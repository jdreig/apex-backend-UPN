package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcategoria;

    private String descripcion;

    // Getters
    public Long getIdcategoria() {
        return idcategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Setters
    public void setIdcategoria(Long idcategoria) {
        this.idcategoria = idcategoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

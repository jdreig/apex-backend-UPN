package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrol;

    private String descripcion;

    private Integer tiporol; // Aquí almacenaremos 1 o 2 según rolEstado

    private Integer estado; // 1=activo, 0=inactivo (opcional)

    // Getters y Setters
    public Long getIdrol() { return idrol; }
    public void setIdrol(Long idrol) { this.idrol = idrol; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getTiporol() { return tiporol; }
    public void setTiporol(Integer tiporol) { this.tiporol = tiporol; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }
}

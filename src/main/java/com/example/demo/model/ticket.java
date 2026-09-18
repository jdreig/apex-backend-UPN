package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idticket;

    private String titulo;
    private String descripcion;

    private LocalDateTime fechacreacion;
    private LocalDateTime fechacierre;

    private Integer prioridad; // Se usará ticketPrioridad
    private Integer estado; // Se usará ticketEstado

    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idcategoria", nullable = false)
    private categoria categoria;

    // Getters y Setters
    public Long getIdticket() { return idticket; }
    public void setIdticket(Long idticket) { this.idticket = idticket; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechacreacion() { return fechacreacion; }
    public void setFechacreacion(LocalDateTime fechacreacion) { this.fechacreacion = fechacreacion; }

    public LocalDateTime getFechacierre() { return fechacierre; }
    public void setFechacierre(LocalDateTime fechacierre) { this.fechacierre = fechacierre; }

    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public cliente getCliente() { return cliente; }
    public void setCliente(cliente cliente) { this.cliente = cliente; }

    public categoria getCategoria() { return categoria; }
    public void setCategoria(categoria categoria) { this.categoria = categoria; }
}

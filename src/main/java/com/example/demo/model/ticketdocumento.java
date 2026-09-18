package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticketdocumento")
public class ticketdocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idticketdocumento;

    private String ruta;
    private Integer estado;
    private Long idticket;

    // Getters
    public Long getIdticketdocumento() {
        return idticketdocumento;
    }

    public String getRuta() {
        return ruta;
    }

    public Integer getEstado() {
        return estado;
    }

    public Long getIdticket() {
        return idticket;
    }

    // Setters
    public void setIdticketdocumento(Long idticketdocumento) {
        this.idticketdocumento = idticketdocumento;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public void setIdticket(Long idticket) {
        this.idticket = idticket;
    }
}

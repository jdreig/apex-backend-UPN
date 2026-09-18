package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "ticketagente")
public class ticketAgente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idticketagente;

    // Usamos Long para almacenar el ID del usuario y ticket
    @JoinColumn(name = "idusuario", nullable = false)
    private Long idusuario;  
    
    @JoinColumn(name = "idticket", nullable = false)
    private Long idticket;  
    
    private String respuesta;
    private LocalDateTime fecharespuesta;

    // Getters y Setters
    public Long getIdticketagente() {
        return idticketagente;
    }

    public void setIdticketagente(Long idticketagente) {
        this.idticketagente = idticketagente;
    }

    public Long getUsuario() {
        return idusuario;
    }

    public void setUsuario(Long usuario) {
        this.idusuario = usuario;
    }

    public Long getTicket() {
        return idticket;
    }

    public void setTicket(Long ticket) {
        this.idticket = ticket;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public LocalDateTime getFechaRespuesta() {
        return fecharespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fecharespuesta) {
        this.fecharespuesta = fecharespuesta;
    }
}

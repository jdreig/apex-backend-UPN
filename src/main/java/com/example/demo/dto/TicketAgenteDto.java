package com.example.demo.dto;

public class TicketAgenteDto {
    private Long idticket;
    private Long idusuario;
    private String respuesta;

    // Getters y Setters
    public Long getIdticket() { return idticket; }
    public void setIdticket(Long idticket) { this.idticket = idticket; }
    public Long getIdusuario() { return idusuario; }
    public void setIdusuario(Long idusuario) { this.idusuario = idusuario; }
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}
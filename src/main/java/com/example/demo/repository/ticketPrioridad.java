package com.example.demo.repository;

public enum ticketPrioridad {
	Baja(1, "Baja"),
    Media(2, "Media"),
    Alta(3, "Alta"),
    Critica(4, "Critico");

    private final int codigo;
    private final String descripcion;

    ticketPrioridad(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static ticketPrioridad fromCodigo(int codigo) {
        for (ticketPrioridad prioridad : ticketPrioridad.values()) {
            if (prioridad.codigo == codigo) {
                return prioridad;
            }
        }
        throw new IllegalArgumentException("Código de prioridad inválido: " + codigo);
    }
}
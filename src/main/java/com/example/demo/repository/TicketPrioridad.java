package com.example.demo.repository;

public enum TicketPrioridad {
    BAJA(1, "Baja"),
    MEDIA(2, "Media"),
    ALTA(3, "Alta"),
    CRITICA(4, "Critico");

    private final int codigo;
    private final String descripcion;

    TicketPrioridad(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TicketPrioridad fromCodigo(int codigo) {
        for (TicketPrioridad prioridad : TicketPrioridad.values()) {
            if (prioridad.codigo == codigo) {
                return prioridad;
            }
        }
        throw new IllegalArgumentException("Código de prioridad inválido: " + codigo);
    }
}
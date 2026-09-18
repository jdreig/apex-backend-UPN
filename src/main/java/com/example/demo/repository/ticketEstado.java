package com.example.demo.repository;

public enum ticketEstado {
    ABIERTO(1, "Abierto"),
    EN_PROCESO(2, "En Proceso"),
    ATENDIDO(3, "Atendido"),
    CERRADO(4, "Cerrado"),
    REABIERTO(5, "Reabierto");

    private final int codigo;
    private final String descripcion;

    ticketEstado(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static ticketEstado fromCodigo(int codigo) {
        for (ticketEstado estado : ticketEstado.values()) {
            if (estado.codigo == codigo) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + codigo);
    }
}
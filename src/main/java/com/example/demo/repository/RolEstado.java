package com.example.demo.repository;

public enum RolEstado {
    SOPORTE(1),
    CLIENTE(2);

    private final int valor;

    RolEstado(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static RolEstado fromValor(int valor) {
        for (RolEstado r : RolEstado.values()) {
            if (r.getValor() == valor) {
                return r;
            }
        }
        throw new IllegalArgumentException("Valor de rol inválido: " + valor);
    }
}

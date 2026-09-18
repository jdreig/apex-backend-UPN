package com.example.demo.repository;

public enum rolEstado {
    SOPORTE(1),
    CLIENTE(2);

    private final int valor;

    rolEstado(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static rolEstado fromValor(int valor) {
        for (rolEstado r : rolEstado.values()) {
            if (r.getValor() == valor) {
                return r;
            }
        }
        throw new IllegalArgumentException("Valor de rol inválido: " + valor);
    }
}

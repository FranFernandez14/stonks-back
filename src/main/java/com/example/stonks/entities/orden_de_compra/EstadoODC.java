package com.example.stonks.entities.orden_de_compra;

public enum EstadoODC {
    SIN_CONFIRMAR(1),
    CONFIRMADA(2),
    ACEPTADA(3),
    EN_CAMINO(4),
    RECIBIDA(5),
    CANCELADA(6);

    private final int numero;

    EstadoODC(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

}

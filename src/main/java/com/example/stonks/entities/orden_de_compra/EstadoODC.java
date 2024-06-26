package com.example.stonks.entities.orden_de_compra;

public enum EstadoODC {
    SIN_CONFIRMAR("Sin confirmar"),
    CONFIRMADA("Confirmada"),
    ACEPTADA("Aceptada"),
    EN_CAMINO("En camino"),
    RECIBIDA("Recibida"),
    CANCELADA("Cancelada");

    private final String estado;

    EstadoODC(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public static EstadoODC fromEstado(String estado) {
        for (EstadoODC value : EstadoODC.values()) {
            if (value.estado.equalsIgnoreCase(estado)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + estado);
    }
}


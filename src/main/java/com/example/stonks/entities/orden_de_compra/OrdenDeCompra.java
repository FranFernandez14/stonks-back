package com.example.stonks.entities.orden_de_compra;

import com.example.stonks.entities.Base;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
public class OrdenDeCompra extends Base {

    private int nroOrdenDeCompra;
    private Date fechaCreacion;
    private Date fechaLlegada;
    private Double costoTotal;
    private Double costoEnvio;

    @Enumerated(EnumType.ORDINAL)
    private EstadoODC estadoActual;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

}

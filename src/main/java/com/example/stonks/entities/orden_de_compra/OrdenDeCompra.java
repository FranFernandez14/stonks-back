package com.example.stonks.entities.orden_de_compra;

import com.example.stonks.entities.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDeCompra extends Base {

    private int nroOrdenDeCompra;
    private Date fechaCreacion;
    private Date fechaLlegada;
    private Double costoTotal;
    private Double costoEnvio;

    @Enumerated(EnumType.ORDINAL)
    private EstadoODC estadoActual;

    @ManyToOne()
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_orden_de_compra")
    List<DetalleOrdenDeCompra> detalles = new ArrayList<DetalleOrdenDeCompra>();

}

package com.example.stonks.entities.orden_de_compra;


import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class DetalleOrdenDeCompra extends Base {


    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
    private int cantidad;
    private Double subTotal;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "id_orden_de_compra")
    private OrdenDeCompra ordenDeCompra;
}

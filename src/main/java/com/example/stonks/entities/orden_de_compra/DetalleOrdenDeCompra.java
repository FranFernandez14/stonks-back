package com.example.stonks.entities.orden_de_compra;


import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detalle_orden_de_compra")
public class DetalleOrdenDeCompra extends Base {


    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
    private int cantidad;
    private Double subTotal;

    private double valorArticulo;
    private double totalDetalle;

}

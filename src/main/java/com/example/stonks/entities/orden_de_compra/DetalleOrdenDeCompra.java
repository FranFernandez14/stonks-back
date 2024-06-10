package com.example.stonks.entities.orden_de_compra;


import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class DetalleOrdenDeCompra extends Base {


    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
    private int cantidad;
    private double valorArticulo;
    private double totalDetalle;

    @ManyToOne
    @JoinColumn(name = "id_orden_de_compra")
    private OrdenDeCompra ordenDeCompra;
}

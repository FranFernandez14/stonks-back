package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class LineaVenta extends Base {

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    private int contadorLineaVenta;
    private double precio;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

}

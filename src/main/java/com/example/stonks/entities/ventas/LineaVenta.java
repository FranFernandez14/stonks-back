package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "linea_venta")
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

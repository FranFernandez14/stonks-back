package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Linea_venta")
@NoArgsConstructor
@AllArgsConstructor
public class LineaVenta extends Base {

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    @Column(name = "contador_linea_venta")
    private int contadorLineaVenta;

    @Column(name = "precio_unitario")
    private double precioUnitario;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "cantidad")
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

}

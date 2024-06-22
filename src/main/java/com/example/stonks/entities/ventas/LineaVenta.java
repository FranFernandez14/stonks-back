package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "linea_venta")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineaVenta extends Base {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    @Column(name = "contador_linea_venta")
    private int contadorLineaVenta;

    @Column(name = "precio_unitario")
    private double precioUnitario;

    @Column(name = "cantidad")
    private int cantidad;
}

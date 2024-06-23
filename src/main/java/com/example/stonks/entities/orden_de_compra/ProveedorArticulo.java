package com.example.stonks.entities.orden_de_compra;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "proveedor_articulo")
public class ProveedorArticulo extends Base {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    private Double precio;



}

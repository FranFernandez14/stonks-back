package com.example.stonks.entities.articulos;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@Table(name = "Articulo")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Articulo extends Base {

    @Column(name = "cod")
    private String cod;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "stockActual")
    private int stockActual;
    @Column(name = "stockSeguridad")
    private int stockSeguridad;
    @Column(name = "loteOptimo")
    private int loteOptimo;
    @Column(name = "puntoPedido")
    private int puntoPedido;
    @Column(name = "precioVenta")
    private int precioVenta;
    @Column(name = "inventarioMaximo")
    private int inventarioMaximo;
    @Column(name = "cp")
    private int cp;
    @Column(name = "ca")
    private int ca;

    @ManyToOne
    @JoinColumn(name = "id_familia_articulo")
    private FamiliaArticulo familiaArticulo;

    @ManyToOne
    @JoinColumn(name = "id_proveedor_predeterminado")
    @JsonIgnoreProperties("proveedorArticulos")
    private Proveedor predeterminado;

    @OneToMany(mappedBy = "articulo")
    private List<Demanda> demandas;

    }

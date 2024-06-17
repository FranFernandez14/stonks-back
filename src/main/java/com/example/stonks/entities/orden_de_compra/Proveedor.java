package com.example.stonks.entities.orden_de_compra;

import com.example.stonks.entities.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor extends Base {

    @Column(name = "cod_proveedor")
    private int cod;

    @Column(name = "nombre_proveedor")
    private String nombre;

    @Column(name = "email_proveedor")
    private String email;

    /*@Column(name = "cantidad_articulos_minimos_proveedor")
    private int cantArticulosMinimos;

    @Column(name = "costo_minimo_proveedor")
    private int costoMinimo;

    @Column(name = "dias_demora_entrega_proveedor")
    private int diasDemoraEntrega;

    @Column(name = "cantidad_articulos_maximos_proveedor")
    private int cantArticulosMaximos;*/

    @Column(name = "dias_demora_entrega_proveedor")
    private int diasDemoraEntrega;

    @Column(name = "costo_envio_proveedor")
    private double costoEnvio;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProveedorArticulo> proveedorArticulos;
}

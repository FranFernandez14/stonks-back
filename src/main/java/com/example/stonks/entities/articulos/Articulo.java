package com.example.stonks.entities.articulos;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Data
@Entity
public class Articulo extends Base {

    private String cod;
    private String nombre;
    private int stockActual;
    private int stockSeguridad;
    private int loteOptimo;
    private int puntoPedido;
    private int precioVenta;
    private int inventarioMaximo;

    @ManyToOne
    @JoinColumn(name = "id_familia_articulo")
    private FamiliaArticulo familiaArticulo;

    @ManyToOne
    @JoinColumn(name = "id_proveedor_predeterminado")
    @JsonIgnoreProperties("proveedorArticulos")
    private Proveedor predeterminado;


}

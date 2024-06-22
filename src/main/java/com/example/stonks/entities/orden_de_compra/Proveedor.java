package com.example.stonks.entities.orden_de_compra;

import com.example.stonks.entities.Base;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.*;
@Entity
@Data
public class Proveedor extends Base {

    private int cod;
    private String nombre;
    private String email;
    private int cantArticulosMinimos;
    private int costoMinimo;
    private int costoEnvio;
    private int diasDemoraEntrega;
    private int cantArticulosMaximos;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProveedorArticulo> proveedorArticulos;
}

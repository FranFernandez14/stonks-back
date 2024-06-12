package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Data
@Entity
public class Venta extends Base {

    private int nroVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "venta")
    private List<LineaVenta> lineasVenta = new ArrayList<>();

    @Column(name = "fecha_venta")
    private Date fechaVenta;
}

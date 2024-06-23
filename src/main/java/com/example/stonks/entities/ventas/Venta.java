package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.*;

@Data
@Entity
@Table(name = "Venta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venta extends Base {

    @Column(name = "nro_venta")
    private int nroVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    private List<LineaVenta> lineasVenta = new ArrayList<>();

    @Column(name = "fecha_venta")
    @Temporal(TemporalType.DATE)
    private Date fechaVenta;
}

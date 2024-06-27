package com.example.stonks.entities.demanda;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demanda")
@Builder
@Getter
@Setter
public class Demanda extends Base {

    @Column(name = "cantidad")
    private float cantidad;
    @Column(name = "mes")
    private int mes;
    @Column(name = "año")
    private int año;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

}

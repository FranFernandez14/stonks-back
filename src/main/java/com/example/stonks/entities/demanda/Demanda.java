package com.example.stonks.entities.demanda;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "demanda")
@Builder
public class Demanda extends Base {

    private float cantidad;
    private int mes;
    private int año;
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
}

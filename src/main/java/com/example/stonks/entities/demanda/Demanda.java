package com.example.stonks.entities.demanda;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Demanda extends Base {

    private int cantidad;
    private int mes;
    private int año;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
}

package com.example.stonks.entities.demanda;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@Table(name = "prediccion")
public class Prediccion extends Base {

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    private float cantidadPredecida;

    private int mes;

    private int año;

}

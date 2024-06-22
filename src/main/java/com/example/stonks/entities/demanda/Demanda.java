package com.example.stonks.entities.demanda;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder

public class Demanda extends Base {

    private int cantidad;
    private int mes;
    private int año;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

}

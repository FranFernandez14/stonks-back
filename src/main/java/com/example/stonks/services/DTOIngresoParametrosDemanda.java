package com.example.stonks.services;

import com.example.stonks.entities.articulos.Articulo;
import lombok.Data;

import java.util.List;
@Data
public class DTOIngresoParametrosDemanda {

    private int cantidadPeriodosAPredecir;

    private float errorAceptable;

    private List<Float> ponderacion;

    private int cantidadPeriodosAUtilizar;

    private float alfa;

    private Articulo articulo;
}

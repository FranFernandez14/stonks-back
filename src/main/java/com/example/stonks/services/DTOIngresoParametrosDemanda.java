package com.example.stonks.services;

import com.example.stonks.entities.articulos.Articulo;
import lombok.Data;

import java.util.List;
@Data
public class DTOIngresoParametrosDemanda {

    //Para todos los métodos
    private Articulo articulo;

    private int cantidadPeriodosAPredecir;

    private int cantidadPeriodosAUtilizar;

    //Para el PMP
    private List<Float> ponderacion;

    private float alfa;

    //Para el método de índices
    private int demandaSiguienteAño;

    private int ciclos;
}

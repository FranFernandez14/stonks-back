package com.example.stonks.services.demanda;

import com.example.stonks.entities.articulos.Articulo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTOIngresoParametrosDemanda {

    //Para todos los métodos
    private Articulo articulo;

    private int cantidadPeriodosAPredecir;

    private int cantidadPeriodosParaError;

    //Para el PMP
    private List<Float> ponderacion;

    //Para suavizacion
    private float alfa;

    //Para el método de índices
    private float demandaAñoAPredecir;

    private int ciclos;
}

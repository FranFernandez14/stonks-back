package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOListaPrediccion {

    private List<DTOPrediccion> listaPrediccion = new ArrayList<DTOPrediccion>();

    private float errorCometido;

    private String estrategia;

    private boolean sePredijo;

    public void add(DTOPrediccion dtoPrediccion) {
        this.listaPrediccion.add(dtoPrediccion);
    }

}

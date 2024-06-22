package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class DTOListaPrediccion {

    private List<DTOPrediccion> listaPrediccion = new ArrayList<DTOPrediccion>();

    private float errorCometido;

    public void add(DTOPrediccion dtoPrediccion) {
        this.listaPrediccion.add(dtoPrediccion);
    }

}

package com.example.stonks.services.estrategiaPredecirDemanda;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class DTOListaPrediccion {

    private List<DTOPrediccion> listaPrediccion = new ArrayList<DTOPrediccion>();

    public void add(DTOPrediccion dtoPrediccion) {
        this.listaPrediccion.add(dtoPrediccion);
    }

}

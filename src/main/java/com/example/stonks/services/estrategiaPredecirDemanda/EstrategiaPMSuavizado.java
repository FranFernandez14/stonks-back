package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;

import java.util.Collection;

public class EstrategiaPMSuavizado implements EstrategiaPredecirDemanda{
    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                              Collection<Demanda> listaDemandasHistoricas) {
        return null;
    }
}

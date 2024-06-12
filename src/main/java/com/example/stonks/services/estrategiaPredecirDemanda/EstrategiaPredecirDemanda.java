package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;

import java.util.Collection;

public interface EstrategiaPredecirDemanda {

    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                              Collection<Demanda> listaDemandasHistoricas) throws Exception;
}

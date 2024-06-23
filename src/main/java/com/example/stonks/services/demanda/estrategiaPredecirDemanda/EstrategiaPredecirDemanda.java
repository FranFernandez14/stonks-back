package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;

public interface EstrategiaPredecirDemanda {

    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception;
}

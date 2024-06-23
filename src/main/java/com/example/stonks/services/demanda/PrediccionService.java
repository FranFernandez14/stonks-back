package com.example.stonks.services.demanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.services.BaseService;

public interface PrediccionService extends BaseService<Prediccion, Long> {

    public DTORetornoPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception;
}

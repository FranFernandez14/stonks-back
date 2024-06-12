package com.example.stonks.services;

import com.example.stonks.entities.demanda.Prediccion;

public interface PrediccionService extends BaseService<Prediccion, Long> {

    public Prediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception;
}

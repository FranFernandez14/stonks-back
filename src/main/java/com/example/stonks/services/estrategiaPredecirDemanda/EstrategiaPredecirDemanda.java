package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public interface EstrategiaPredecirDemanda {

    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception;
}

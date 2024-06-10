package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.entities.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.repositories.PrediccionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

public interface EstrategiaPredecirDemanda {

    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                              Collection<Demanda> listaDemandasHistoricas);
}

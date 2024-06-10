package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.entities.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.repositories.PrediccionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class EstrategiaPromedioMovil implements EstrategiaPredecirDemanda{

    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                              Collection<Demanda> listaDemandasHistoricas) {

        DTOListaPrediccion listaPrediccion = new DTOListaPrediccion();

        int periodosAPredecir = dtoIngresoParametrosDemanda.getCantidadPeriodosAPredecir();

        int periodosAUsarCalculo = dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar();

        for(int i = 0; i < periodosAPredecir; i++) {



        }

        return listaPrediccion;
    }
}

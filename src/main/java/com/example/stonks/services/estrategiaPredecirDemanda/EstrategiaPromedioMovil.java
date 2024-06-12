package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.abs;

public class EstrategiaPromedioMovil implements EstrategiaPredecirDemanda{

    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                              Collection<Demanda> listaDemandasHistoricas) {

        List <Demanda> listaDemandasHistoricasArray = new ArrayList<>(listaDemandasHistoricas);

        int periodosAPredecir = dtoIngresoParametrosDemanda.getCantidadPeriodosAPredecir();

        int periodosAUsarCalculo = dtoIngresoParametrosDemanda.getPonderacion().size();

        int cantidadIteraciones = dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar()
                                    - periodosAUsarCalculo + periodosAPredecir;

        DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

        for(int i = 0; i < cantidadIteraciones; i++) {

            List<Demanda> demandasParaCalculo = listaDemandasHistoricasArray.subList(
                                                                                    i, //inicio
                                                                                    i + periodosAUsarCalculo /*fin*/);
            float sumatoria = 0;

            for (int j = 0; j < periodosAUsarCalculo; j++) {
                sumatoria += (demandasParaCalculo.get(j).getCantidad()
                            * dtoIngresoParametrosDemanda.getPonderacion().get(j));
            }

            float promedioMovilPonderado = sumatoria / periodosAUsarCalculo;
            float errorCometido = abs(promedioMovilPonderado -
                    listaDemandasHistoricasArray.get(i + periodosAUsarCalculo).getCantidad());
            
            //Si es una prediccion sin datos historicos a contrastar
            if (i >= cantidadIteraciones - periodosAPredecir) {
                errorCometido = 0;
                //Añadimos la prediccion a la lista para incluirla en el calculo de
                //la proxima iteracion
                Demanda demandaPredicha = Demanda.builder()
                                          .cantidad(promedioMovilPonderado)
                                          .build();
                listaDemandasHistoricasArray.add(demandaPredicha);
            }

            resultadoPrediccion.add(new DTOPrediccion(promedioMovilPonderado, errorCometido));
        }

        return resultadoPrediccion;
    }
}

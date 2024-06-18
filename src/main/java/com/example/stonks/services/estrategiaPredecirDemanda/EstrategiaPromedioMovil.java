package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class EstrategiaPromedioMovil implements EstrategiaPredecirDemanda{

    @Autowired
    private PrediccionRepository prediccionRepository;

    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception {
        try {

            int periodosAPredecir = dtoIngresoParametrosDemanda.getCantidadPeriodosAPredecir();

            int cantTerminosPonderacion = dtoIngresoParametrosDemanda.getPonderacion().size();

            int periodosAUtilizar = dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar();

            int cantidadIteraciones = periodosAUtilizar + periodosAPredecir;

            List<Float> ponderaciones = dtoIngresoParametrosDemanda.getPonderacion();

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            //Obtenemos las demandas historicas
            ArrayList<Demanda> listaDemandasCalculo = this.prediccionRepository.getDemandasAnteriores(
                    periodosAPredecir + cantTerminosPonderacion,
                    dtoIngresoParametrosDemanda.getArticulo().getId());

            //POR CADA prediccion
            for (int i = 0; i < cantidadIteraciones; i++) {

                float sumatoria = 0;

                //Agarramos las N demandas previas para calcular la predicción
                for (int j = i; j < cantTerminosPonderacion; j++) {
                    sumatoria += (listaDemandasCalculo.get(j).getCantidad()
                                  * ponderaciones.get(j));
                }
                float promedioMovilPonderado = sumatoria / cantTerminosPonderacion;
                float errorCometido = abs(promedioMovilPonderado -
                                          listaDemandasCalculo.get(i + cantTerminosPonderacion).getCantidad());

                //Si es una prediccion sin datos historicos a contrastar
                if (i >= periodosAUtilizar) {
                    errorCometido = 0;
                /* Añadimos la prediccion a la lista para incluirla en el calculo de la proxima iteracion */
                    Demanda demandaPredicha = Demanda.builder()
                                            .cantidad(promedioMovilPonderado)
                                            .build();
                    listaDemandasCalculo.add(demandaPredicha);
                }
                resultadoPrediccion.add(new DTOPrediccion(promedioMovilPonderado, errorCometido));
            }

            return resultadoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

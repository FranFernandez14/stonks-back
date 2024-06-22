package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
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

            int cantPeriodosParaError = dtoIngresoParametrosDemanda.getCantidadPeriodosParaError();

            int cantidadIteraciones = cantPeriodosParaError + periodosAPredecir;

            List<Float> ponderaciones = dtoIngresoParametrosDemanda.getPonderacion();

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            //Obtenemos las demandas historicas
            ArrayList<Demanda> listaDemandasCalculo = this.prediccionRepository.getDemandasAnteriores(
                    periodosAPredecir + cantTerminosPonderacion,
                    dtoIngresoParametrosDemanda.getArticulo().getId());

            //La query retorna en sentido descendente para obtener las demandas desde la más reciente
            //Pero queremos recorrerla desde la más vieja, por eso la invertimos
            //No la podemos retornar ascendente porque entonces retornamos las primeras N demandas más antiguas
            Collections.reverse(listaDemandasCalculo);

            float errorCometido = 0;
            //POR CADA prediccion
            for (int i = 0; i < cantidadIteraciones; i++) {

                float sumatoria = 0;

                //Agarramos las N demandas previas para calcular la predicción
                for (int j = i; j < cantTerminosPonderacion; j++) {
                    sumatoria += (listaDemandasCalculo.get(j).getCantidad()
                                  * ponderaciones.get(j));
                }
                float promedioMovilPonderado = sumatoria / cantTerminosPonderacion;

                //A la nueva predicción, debemos asignarle el periodo mes-año que corresponde
                //Que siempre va a ser la de la demanda anterior + 1
                //Obtenemos el índice de la demanda anterior a la que estamos prediciendo
                int indiceDemandaAnterior = i + cantTerminosPonderacion - 1;

                int mes = listaDemandasCalculo.get(indiceDemandaAnterior).getMes() + 1;
                int año = listaDemandasCalculo.get(indiceDemandaAnterior).getAño();

                if (mes > 12) {
                    mes -= 12;
                    año++;
                }

                //Si es una prediccion sin datos historicos a contrastar
                if (i >= cantPeriodosParaError) {
                // Añadimos la prediccion a la lista para incluirla en el calculo de la proxima iteracion
                    Demanda demandaPredicha = Demanda.builder()
                                            .cantidad(promedioMovilPonderado)
                                            .año(año)
                                            .mes(mes)
                                            .build();
                    listaDemandasCalculo.add(demandaPredicha);
                }
                //En el caso que no haya demanda historica, tomaria la que acabamos de añadir en el bloque IF
                // por lo tanto el error daría 0 y no afecta a la sumatoria
                errorCometido += abs(promedioMovilPonderado -
                                    listaDemandasCalculo.get(i + cantTerminosPonderacion).getCantidad());

                resultadoPrediccion.add(new DTOPrediccion(promedioMovilPonderado, mes, año));
            }

            resultadoPrediccion.setErrorCometido(errorCometido);
            return resultadoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

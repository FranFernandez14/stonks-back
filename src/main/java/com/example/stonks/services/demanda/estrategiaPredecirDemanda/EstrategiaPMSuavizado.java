package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;

public class EstrategiaPMSuavizado implements EstrategiaPredecirDemanda{
    @Autowired
    private PrediccionRepository prediccionRepository;

    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception {
        try {
            float alfa = dtoIngresoParametrosDemanda.getAlfa();

            int mesActual = Calendar.getInstance().get(Calendar.MONTH);
            int añoActual = Calendar.getInstance().get(Calendar.YEAR);
            /*
            Necesitamos una primera predicción para poder usar el método de suavización
            Esa predicción es la anterior a la que vamos a calcular
            Ya que para la predicción del periodo N, necesitamos la predicción y demanda del periodo N-1
            Se obtiene el periodo base con esa cuenta porque desde el periodo en el que estamos parados, hay que retroceder
            N periodos para estar posicionados sobre la primera predicción que vamos a realizar, y retrocedemos
            un periodo más para obtener los datos para calcular dicha prediccion
            */
            int mesBase = mesActual - dtoIngresoParametrosDemanda.getCantidadPeriodosParaError() - 1;
            int añoBase = añoActual;

            //Ajustes necesarios por si al retroceder entre periodos cambiamos de año
            if (mesBase <= 0) {
                mesBase += 12;
                añoBase--;
            }
            //Obtenemos los datos base para la primera iteracion del bucle
            Prediccion prediccionBase = this.prediccionRepository.getPrediccionByFecha(
                    dtoIngresoParametrosDemanda.getArticulo().getId(),
                    mesBase,
                    añoBase);
            //Obtenemos las demandas historicas
            ArrayList<Demanda> listaDemandasCalculo = this.prediccionRepository.getDemandasAnteriores(
                    dtoIngresoParametrosDemanda.getCantidadPeriodosParaError() + 1,
                    dtoIngresoParametrosDemanda.getArticulo().getId());

            //La query retorna en sentido descendente para obtener las demandas desde la más reciente
            //Pero queremos recorrerla desde la más vieja, por eso la invertimos
            //No la podemos retornar ascendente porque entonces retornamos las primeras N demandas más antiguas
            Collections.reverse(listaDemandasCalculo);

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            List<Prediccion> listaPredicciones = new ArrayList<Prediccion>();

            listaPredicciones.add(prediccionBase);

            int cantIteraciones = dtoIngresoParametrosDemanda.getCantidadPeriodosAPredecir() +
                                dtoIngresoParametrosDemanda.getCantidadPeriodosParaError();

            float errorCometido = 0;

            for (int i = 0; i < cantIteraciones; i++) {

                //Cálculo por Metodo de suavización
                float cantidadPredecida = listaPredicciones.get(i).getCantidadPredecida() + alfa *
                                        (listaDemandasCalculo.get(i).getCantidad() - listaPredicciones.get(i).getCantidadPredecida());

                errorCometido += abs(listaDemandasCalculo.get(i+1).getCantidad() - cantidadPredecida);

                //Para que esta prediccion sea usada en la siguiente iteracion
                listaPredicciones.add(Prediccion.builder().cantidadPredecida(cantidadPredecida).build());

                //Para settear el periodo de la prediccion
                int mes = listaDemandasCalculo.get(i).getMes() + 1;
                int año = listaDemandasCalculo.get(i).getAño();
                if (mes > 12) {
                    mes -= 12;
                    año++;
                }

                //Cuando se agoten las demandas reales, empiezo a usar predicciones
                if (i >= dtoIngresoParametrosDemanda.getCantidadPeriodosParaError()) {
                    listaDemandasCalculo.add(Demanda.builder().
                                    cantidad(cantidadPredecida).
                                    mes(mes).
                                    año(año).
                                    build());
                }

                //Añado la prediccion a la lista resultado
                DTOPrediccion prediccion = new DTOPrediccion(cantidadPredecida, mes, año);
                resultadoPrediccion.add(prediccion);

            }

            resultadoPrediccion.setErrorCometido(errorCometido);
            return resultadoPrediccion;

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}

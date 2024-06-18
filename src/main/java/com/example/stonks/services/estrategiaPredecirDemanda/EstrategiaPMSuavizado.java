package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
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
            int mesBase = mesActual - dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar() - 1;
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
                    dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar() + 1,
                    dtoIngresoParametrosDemanda.getArticulo().getId());

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            List<Prediccion> listaPredicciones = new ArrayList<Prediccion>();

            listaPredicciones.add(prediccionBase);

            int cantIteraciones = dtoIngresoParametrosDemanda.getCantidadPeriodosAPredecir() +
                                dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar();

            for (int i = 0; i < cantIteraciones; i++) {

                //Cálculo por Metodo de suavización
                float cantidadPredecida = listaPredicciones.get(i).getCantidadPredecida() + alfa *
                                        (listaDemandasCalculo.get(i).getCantidad() - listaPredicciones.get(i).getCantidadPredecida());

                float errorCometido = abs(listaDemandasCalculo.get(i+1).getCantidad() - cantidadPredecida);

                //Añado la prediccion a la lista resultado
                DTOPrediccion prediccion = new DTOPrediccion(cantidadPredecida, errorCometido);
                resultadoPrediccion.add(prediccion);

                //Para que esta prediccion sea usada en la siguiente iteracion
                listaPredicciones.add(Prediccion.builder().cantidadPredecida(cantidadPredecida).build());

                //Cuando se agoten las demandas reales, empiezo a usar predicciones
                if (i >= dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar()) {
                    listaDemandasCalculo.add(Demanda.builder().
                                    cantidad(cantidadPredecida).
                                    build());
                }
            }

            return resultadoPrediccion;

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}

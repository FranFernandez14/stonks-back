package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.lang.Math.abs;

@Component
public class EstrategiaPMSuavizado implements EstrategiaPredecirDemanda{
    @Autowired
    private PrediccionRepository prediccionRepository;

    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception {
        try {
            System.out.println("Hola desde suavizado");
            float alfa = dtoIngresoParametrosDemanda.getAlfa();

            //Por qué + 1? Porque por algun motivo siendo hoy 26/06 (junio)
            //retorna que el mes actual es 5 ¿?
            int mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1;
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
            Optional<Prediccion> prediccionBase = this.prediccionRepository.getPrediccionByFecha(
                    dtoIngresoParametrosDemanda.getArticulo().getId(),
                    mesBase,
                    añoBase);

            if (prediccionBase.isEmpty()) throw new Exception("No se encuentran predicciones disponibles");

            //Obtenemos las demandas historicas
            ArrayList<Demanda> listaDemandasCalculo = this.prediccionRepository.getDemandasAnteriores(
                    dtoIngresoParametrosDemanda.getArticulo(),
                    Limit.of(dtoIngresoParametrosDemanda.getCantidadPeriodosParaError() + 1));

            if (listaDemandasCalculo.isEmpty()) throw new Exception("No se encuentran demandas registradas");

            //La query retorna en sentido descendente para obtener las demandas desde la más reciente
            //Pero queremos recorrerla desde la más vieja, por eso la invertimos
            //No la podemos retornar ascendente porque entonces retornamos las primeras N demandas más antiguas
            Collections.reverse(listaDemandasCalculo);

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            List<Prediccion> listaPredicciones = new ArrayList<Prediccion>();

            listaPredicciones.add(prediccionBase.get());

            int cantIteraciones = dtoIngresoParametrosDemanda.getCantidadPeriodosAPredecir() +
                                dtoIngresoParametrosDemanda.getCantidadPeriodosParaError();

            float errorCometido = 0;

            for (int i = 0; i < cantIteraciones; i++) {

                //Cálculo por Metodo de suavización

                float cantidadPredecida = listaPredicciones.get(i).getCantidadPredecida() + alfa *
                                        (listaDemandasCalculo.get(i).getCantidad() - listaPredicciones.get(i).getCantidadPredecida());

                //Para que esta prediccion sea usada en la siguiente iteracion
                listaPredicciones.add(Prediccion.builder().cantidadPredecida(cantidadPredecida).build());

                //Para settear el periodo de la prediccion

                if (listaDemandasCalculo.get(i).getMes() != listaPredicciones.get(i).getMes() ||
                    listaDemandasCalculo.get(i).getAño() != listaPredicciones.get(i).getAño())
                    return DTOListaPrediccion.builder()
                            .estrategia("ESTRATEGIA_SUAVIZACION")
                            .sePredijo(false)
                            .build();

                int mes = listaDemandasCalculo.get(i).getMes() + 1;
                int año = listaDemandasCalculo.get(i).getAño();
                if (mes > 12) {
                    mes -= 12;
                    año++;
                }

                //Cuando se agoten las demandas reales, empiezo a usar predicciones
                if (i >= dtoIngresoParametrosDemanda.getCantidadPeriodosParaError()) {
                    listaDemandasCalculo.add(Demanda.builder().
                                    cantidad((int) cantidadPredecida).
                                    mes(mes).
                                    año(año).
                                    build());
                }

                errorCometido += abs(listaDemandasCalculo.get(i+1).getCantidad() - cantidadPredecida);

                DTOPrediccion prediccion = new DTOPrediccion(cantidadPredecida, mes, año);
                resultadoPrediccion.add(prediccion);

            }

            resultadoPrediccion.setErrorCometido(errorCometido);
            resultadoPrediccion.setEstrategia("ESTRATEGIA_SUAVIZACION");
            resultadoPrediccion.setSePredijo(true);
            return resultadoPrediccion;

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }
}

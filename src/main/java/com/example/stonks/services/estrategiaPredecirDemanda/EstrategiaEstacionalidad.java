package com.example.stonks.services.estrategiaPredecirDemanda;

import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class EstrategiaEstacionalidad implements EstrategiaPredecirDemanda{

    @Autowired
    private PrediccionRepository prediccionRepository;

    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception{
        try {

            int periodosAUsar = dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar();

            int añoHasta = Calendar.getInstance().get(Calendar.YEAR);

            int añoDesde = añoHasta - dtoIngresoParametrosDemanda.getCiclos();

            //Obtener las demandas anteriores
            // TODO: terminar de arreglar la query
            ArrayList<Demanda> listaDemandasCalculo = this.prediccionRepository.getDemandasAnterioresEstacionalidad(
                    periodosAUsar,
                    dtoIngresoParametrosDemanda.getArticulo().getId(),
                    añoDesde,
                    añoHasta);

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            /*
            TODO:
             Tengo que agarrar las demandas POR MES y calcular el promedio
            con esos datos, calculo el promedio de los promedios
            luego divido cada promedio por el promedio de promedios, ese resultado me da el índice de ese mes
            entonces el resultado de la prediccion seria el índice multiplicado por la demanda esperada
            para el año que viene.
            Tengo que hacer la prediccion para el año siguiente y para el anterior (para poder calcular el error)
            */

            return resultadoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

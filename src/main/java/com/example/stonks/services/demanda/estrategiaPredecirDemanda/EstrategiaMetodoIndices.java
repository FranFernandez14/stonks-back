package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.abs;

public class EstrategiaMetodoIndices implements EstrategiaPredecirDemanda{

    @Autowired
    private PrediccionRepository prediccionRepository;

    private DTOListaPrediccion realizarCalculosPrediccion(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                                          int añoHasta) throws Exception{
        try {
            float demandaEsperada = dtoIngresoParametrosDemanda.getDemandaAñoAPredecir();

            int añoDesde = añoHasta - dtoIngresoParametrosDemanda.getCiclos();

            //Obtener las demandas anteriores
            ArrayList<DTOPromedioDemanda> listaPromediosPorMes = this.prediccionRepository.getDemandasPromediosPorMes(
                    dtoIngresoParametrosDemanda.getArticulo().getId(),
                    añoDesde,
                    añoHasta);

            //Calculo del promedio de los promedios (?
            // ¿por que como double y no como float? porque la funcion stream no tiene para mapear a float xd
            float promedioDePromedios = (float) listaPromediosPorMes.stream()
                    .mapToDouble(DTOPromedioDemanda::getPromedioMes)
                    .average()
                    .orElse(0);

            DTOListaPrediccion resultadoPrediccion = new DTOListaPrediccion();

            //Generar la prediccion
            int mes = 12;
            for (DTOPromedioDemanda promedio : listaPromediosPorMes) {
                resultadoPrediccion.add(DTOPrediccion.builder()
                        .valorPrediccion((float)(demandaEsperada * (promedio.getPromedioMes() / promedioDePromedios)))
                        .año(añoHasta + 1)
                        .mes(mes)
                        .build());
                mes--;
            }

            return resultadoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public DTOListaPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception{
        try {

            DTOIngresoParametrosDemanda dtoParametrosAñoAnterior = dtoIngresoParametrosDemanda;

            int añoHastaPrediccion = Calendar.getInstance().get(Calendar.YEAR) - 1;
            int añoHastaParaError = añoHastaPrediccion - 1;

            //Realizar la sumatoria de todas las demandas del año anterior (para el error)
            List<Demanda> listaDemandasAñoAnterior = this.prediccionRepository.getDemandasByAño(
                    dtoParametrosAñoAnterior.getArticulo().getId(),
                    añoHastaParaError);

            float demandaTotalAñoAnterior = listaDemandasAñoAnterior.stream()
                    .map(Demanda::getCantidad)
                    .reduce((int) 0f, Integer::sum);

            dtoParametrosAñoAnterior.setDemandaAñoAPredecir(demandaTotalAñoAnterior);

            DTOListaPrediccion resultadoPrediccionAñoAnterior = this.realizarCalculosPrediccion(dtoParametrosAñoAnterior, añoHastaParaError);

            DTOListaPrediccion resultadoPrediccion = this.realizarCalculosPrediccion(dtoIngresoParametrosDemanda, añoHastaPrediccion);

            float errorCometido = 0;
            //Obtener la sumatoria de los errores
            for (int i = 0; i < resultadoPrediccionAñoAnterior.getListaPrediccion().size(); i++) {
                errorCometido += abs(resultadoPrediccionAñoAnterior.getListaPrediccion().get(i).getValorPrediccion()
                                     - listaDemandasAñoAnterior.get(i).getCantidad());
            }

            resultadoPrediccion.setErrorCometido(errorCometido);
            return resultadoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

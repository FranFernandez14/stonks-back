package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.abs;

@Component
public class EstrategiaMetodoIndices implements EstrategiaPredecirDemanda{

    @Autowired
    private PrediccionRepository prediccionRepository;

    private DTOListaPrediccion realizarCalculosPrediccion(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda,
                                                          int añoHasta) throws Exception{
        try {
            float demandaEsperadaMensual = dtoIngresoParametrosDemanda.getDemandaAñoAPredecir()/12;

            int añoDesde = añoHasta - dtoIngresoParametrosDemanda.getCiclos();

            //Obtener las demandas anteriores
            ArrayList<DTOPromedioDemanda> listaPromediosPorMes = this.prediccionRepository.getDemandasPromediosPorMes(
                    dtoIngresoParametrosDemanda.getArticulo().getId(),
                    añoDesde,
                    añoHasta);
            if (listaPromediosPorMes.size() != 12) throw new Exception("No hay suficientes demandas registradas para realizar el método de índices");

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
                        .valorPrediccion((float)(demandaEsperadaMensual * (promedio.getPromedioMes() / promedioDePromedios)))
                        .año(añoHasta + 1)
                        .mes(mes)
                        .build());
                System.out.println(demandaEsperadaMensual * (promedio.getPromedioMes() / promedioDePromedios));
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

            if (listaDemandasAñoAnterior.isEmpty()) throw new Exception("No hay demandas registradas");

            float demandaTotalAñoAnterior = listaDemandasAñoAnterior.stream()
                    .map(Demanda::getCantidad)
                    .reduce(0f, Float::sum);

            dtoParametrosAñoAnterior.setDemandaAñoAPredecir(demandaTotalAñoAnterior);

            DTOListaPrediccion resultadoPrediccionAñoAnterior = this.realizarCalculosPrediccion(dtoParametrosAñoAnterior, añoHastaParaError);

            DTOListaPrediccion resultadoPrediccion = this.realizarCalculosPrediccion(dtoIngresoParametrosDemanda, añoHastaPrediccion);

            resultadoPrediccion.setEstrategia("ESTRATEGIA_INDICES");

            float errorCometido = 0;

            //Obtener la sumatoria de los errores
            for (int i = 0; i < resultadoPrediccionAñoAnterior.getListaPrediccion().size(); i++) {
                errorCometido += abs(resultadoPrediccionAñoAnterior.getListaPrediccion().get(i).getValorPrediccion()
                                     - listaDemandasAñoAnterior.get(i).getCantidad());
            }

            resultadoPrediccion.setErrorCometido(errorCometido);
            resultadoPrediccion.setSePredijo(true);
            return resultadoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

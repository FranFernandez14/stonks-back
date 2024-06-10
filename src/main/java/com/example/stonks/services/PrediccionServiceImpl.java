package com.example.stonks.services;

import com.example.stonks.entities.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.estrategiaPredecirDemanda.EstrategiaPredecirDemanda;
import com.example.stonks.services.estrategiaPredecirDemanda.FactoriaEstrategiaPredecirDemanda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PrediccionServiceImpl extends BaseServiceImpl<Prediccion, Long> implements PrediccionService {

    @Autowired
    private final PrediccionRepository prediccionRepository;

    public PrediccionServiceImpl(
            BaseRepository<Prediccion, Long> baseRepository,
            PrediccionRepository prediccionRepository) {
        super(baseRepository);
        this.prediccionRepository = prediccionRepository;
    }

    @Override
    public Prediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) {

        Collection<Demanda> listaDemandas = this.prediccionRepository.getDemandas(
                dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar(),
                dtoIngresoParametrosDemanda.getArticulo().getId());

        List<EstrategiaPredecirDemanda> listaEstrategias = FactoriaEstrategiaPredecirDemanda.getInstance().obtenerEstrategias();

        for (EstrategiaPredecirDemanda estrategia : listaEstrategias) {
            /* TODO
            1- Definir que retornan las estrategias. Para eso necesito saber como calcular el error
            y si se puede predecir para periodos donde no hay demanda historica
            2- Con eso resuelto, agarrar la mejor de las predicciones segun la que tenga menor error
            y retornarla
            3- Preguntar: suponiendo que sean varias predicciones, como retorno la del mejor error si
            por cada metodo van a haber varios errores, no es comparar uno a uno sino varios con varios,
            qué criterio usaria?
            4- Preguntar: es necesario calcular predicciones de mas de un periodo?
            5- Luego de retornar la supuesta mejor prediccion, hay que generar una orden de compra
            (si es que no existe una pendiente), verificando el stock del producto. ¿Qué hay que
            verificar de ese stock?
            6- Generar una demanda a partir de ventas vs hardcodeado, de momento solo se puede
            cargar demandas hardcodeadas, pero falta recoger las ventas y generar una si es que
            no existe una previa
            7- Terminar de codear el algoritmo de c/ estrategia
            */
        }

        return null;

    }
}

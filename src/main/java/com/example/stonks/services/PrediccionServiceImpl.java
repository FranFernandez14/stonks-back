package com.example.stonks.services;

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
    public Prediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception{

        if (dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar()
                >
            dtoIngresoParametrosDemanda.getPonderacion().size()) {
            throw new Exception();
        }

        Collection<Demanda> listaDemandas = this.prediccionRepository.getDemandas(
                dtoIngresoParametrosDemanda.getCantidadPeriodosAUtilizar(),
                dtoIngresoParametrosDemanda.getArticulo().getId());

        List<EstrategiaPredecirDemanda> listaEstrategias = FactoriaEstrategiaPredecirDemanda.getInstance().obtenerEstrategias();

        for (EstrategiaPredecirDemanda estrategia : listaEstrategias) {
            /* TODO
            2- Agarrar la mejor de las predicciones segun la que tenga menor error y retornarla
            5- Luego de retornar la supuesta mejor prediccion, hay que generar una orden de compra
            (si es que no existe una pendiente)
            */
        }

        return null;

    }
}

package com.example.stonks.services.demanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.entities.orden_de_compra.*;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOListaPrediccion;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOPrediccion;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.EstrategiaPredecirDemanda;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.FactoriaEstrategiaPredecirDemanda;
import com.example.stonks.services.orden_de_compra.OrdenDeCompraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PrediccionServiceImpl extends BaseServiceImpl<Prediccion, Long> implements PrediccionService {

    @Autowired
    private final PrediccionRepository prediccionRepository;

    @Autowired
    private final OrdenDeCompraServiceImpl ordenDeCompraServiceImpl;


    public PrediccionServiceImpl(
            BaseRepository<Prediccion, Long> baseRepository,
            PrediccionRepository prediccionRepository,
            OrdenDeCompraServiceImpl ordenDeCompraService) {
        super(baseRepository);
        this.ordenDeCompraServiceImpl = ordenDeCompraService;
        this.prediccionRepository = prediccionRepository;
    }

    @Override
    public DTORetornoPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception{
        try {
            if (dtoIngresoParametrosDemanda.getAlfa() < 0 ||
                    dtoIngresoParametrosDemanda.getAlfa() > 1)
                throw new Exception("Alfa debe estar entre 0 y 1");

            if (dtoIngresoParametrosDemanda.getCiclos() < 3 ) throw new Exception("La cantidad de ciclos debe ser al menos 3");

            List<EstrategiaPredecirDemanda> listaEstrategias = FactoriaEstrategiaPredecirDemanda.getInstance().obtenerEstrategias();

            List<DTOListaPrediccion> listaPredicciones = new ArrayList<DTOListaPrediccion>();

            for (EstrategiaPredecirDemanda estrategia : listaEstrategias) {
                listaPredicciones.add(estrategia.predecirDemanda(dtoIngresoParametrosDemanda));
            }

            listaPredicciones.sort(Comparator.comparing(DTOListaPrediccion::getErrorCometido));
            DTOListaPrediccion prediccionGanadora = listaPredicciones.get(0);

            List<Integer> listaMeses = prediccionGanadora.getListaPrediccion()
                    .stream()
                    .map(DTOPrediccion::getMes)
                    .distinct()
                    .toList();

            List<Integer> listaAños = prediccionGanadora.getListaPrediccion()
                    .stream()
                    .map(DTOPrediccion::getAño)
                    .distinct()
                    .toList();

            ArrayList<Prediccion> prediccionesExistentes = this.prediccionRepository.findByMesInAndAñoInAndArticulo(
                    listaMeses,
                    listaAños,
                    dtoIngresoParametrosDemanda.getArticulo()
            );

            //Armamos un map que contiene como clave la dupla mes-año y como valor la predicción
            Map<String, DTOPrediccion> nuevasPrediccionesMap = prediccionGanadora.getListaPrediccion().stream()
                    .collect(Collectors.toMap(p -> p.getMes() + "-" + p.getAño(), p -> p));

            //Si ya existía la predicción anteriormente, la borramos
            prediccionesExistentes.forEach(prediccionExistente -> {
                String key = prediccionExistente.getMes() + "-" + prediccionExistente.getAño();
                if (nuevasPrediccionesMap.containsKey(key)) {
                    prediccionRepository.delete(prediccionExistente);
                }
            });

            List<Prediccion> prediccionesParaPersistir = new ArrayList<>();

            for (DTOPrediccion dto : prediccionGanadora.getListaPrediccion()) {
                Prediccion entity = Prediccion.builder()
                        .mes(dto.getMes())
                        .año(dto.getAño())
                        .articulo(dtoIngresoParametrosDemanda.getArticulo())
                        .build();

                prediccionesParaPersistir.add(entity);
            }

            this.prediccionRepository.saveAll(prediccionesParaPersistir);

            OrdenDeCompra ordenDeCompra = this.ordenDeCompraServiceImpl.generarOrdenDeCompra(
                    dtoIngresoParametrosDemanda.getArticulo().getId(),
                    dtoIngresoParametrosDemanda.getArticulo().getPredeterminado().getId()
            );

            DTORetornoPrediccion dtoRetornoPrediccion = DTORetornoPrediccion.builder()
                    .listaPrediccion(prediccionesParaPersistir)
                    .ordenDeCompra(ordenDeCompra).build();

            return dtoRetornoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

package com.example.stonks.services.demanda;

import com.example.stonks.entities.articulos.Articulo;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrediccionServiceImpl extends BaseServiceImpl<Prediccion, Long> implements PrediccionService {

    @Autowired
    private final PrediccionRepository prediccionRepository;

    @Autowired
    private final OrdenDeCompraServiceImpl ordenDeCompraServiceImpl;

    @Autowired
    private final FactoriaEstrategiaPredecirDemanda factoriaEstrategiaPredecirDemanda;


    public PrediccionServiceImpl(
            BaseRepository<Prediccion, Long> baseRepository,
            PrediccionRepository prediccionRepository,
            OrdenDeCompraServiceImpl ordenDeCompraService,
            FactoriaEstrategiaPredecirDemanda factoriaEstrategiaPredecirDemanda) {
        super(baseRepository);
        this.ordenDeCompraServiceImpl = ordenDeCompraService;
        this.prediccionRepository = prediccionRepository;
        this.factoriaEstrategiaPredecirDemanda = factoriaEstrategiaPredecirDemanda;
    }

    @Override
    //Implementa baja lógica en vez de fisica
    public boolean delete(Long id) throws Exception {

        Optional<Prediccion> prediccion = this.prediccionRepository.findById(id);

        if (prediccion.isEmpty()) {
            return false;
        } else {
            prediccion.get().setFechaBaja(new Date());
            this.prediccionRepository.save(prediccion.get());
            return true;
        }
    }

    @Override
    public DTORetornoPrediccion predecirDemanda(DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda) throws Exception{
        try {

            if (dtoIngresoParametrosDemanda.getAlfa() < 0 ||
                    dtoIngresoParametrosDemanda.getAlfa() > 1)
                throw new Exception("Alfa debe estar entre 0 y 1");

            if (dtoIngresoParametrosDemanda.getCiclos() < 3 ) throw new Exception("La cantidad de ciclos debe ser al menos 3");

            if (dtoIngresoParametrosDemanda.getPonderacion().size() > dtoIngresoParametrosDemanda.getCantidadPeriodosParaError())
                throw new Exception("Demasiadas ponderaciones para la cantidad de periodos a predecir");

            if (dtoIngresoParametrosDemanda.getPonderacion().isEmpty()) throw new Exception("No se envio ninguna ponderacion");

            List<EstrategiaPredecirDemanda> listaEstrategias = this.factoriaEstrategiaPredecirDemanda.obtenerEstrategias();

            List<DTOListaPrediccion> listaPredicciones = new ArrayList<DTOListaPrediccion>();

            for (EstrategiaPredecirDemanda estrategia : listaEstrategias) {
                listaPredicciones.add(estrategia.predecirDemanda(dtoIngresoParametrosDemanda));
            }

            Optional<DTOListaPrediccion> prediccionGanadora = listaPredicciones.stream()
                    .filter(DTOListaPrediccion::isSePredijo) // Filtrar por sePredijo == true
                    .min(Comparator.comparing(DTOListaPrediccion::getErrorCometido)); // Encontrar el de menor errorCometido

            if (prediccionGanadora.isEmpty()) throw new Exception("No se pudo realizar ninguna prediccion");

            List<Integer> listaMeses = prediccionGanadora.get().getListaPrediccion()
                    .stream()
                    .map(DTOPrediccion::getMes)
                    .distinct()
                    .toList();

            List<Integer> listaAños = prediccionGanadora.get().getListaPrediccion()
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
            Map<String, DTOPrediccion> nuevasPrediccionesMap = prediccionGanadora.get().getListaPrediccion().stream()
                    .collect(Collectors.toMap(p -> p.getMes() + "-" + p.getAño(), p -> p));

            //Si ya existía la predicción anteriormente, la borramos
            prediccionesExistentes.forEach(prediccionExistente -> {
                String key = prediccionExistente.getMes() + "-" + prediccionExistente.getAño();
                if (nuevasPrediccionesMap.containsKey(key)) {
                    try {
                        this.delete(prediccionExistente.getId());
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            });

            List<Prediccion> prediccionesParaPersistir = new ArrayList<>();

            for (DTOPrediccion dto : prediccionGanadora.get().getListaPrediccion()) {
                Prediccion entity = Prediccion.builder()
                        .cantidadPredecida(dto.getValorPrediccion())
                        .mes(dto.getMes())
                        .año(dto.getAño())
                        .articulo(dtoIngresoParametrosDemanda.getArticulo())
                        .build();

                prediccionesParaPersistir.add(entity);
            }

            this.prediccionRepository.saveAll(prediccionesParaPersistir);

            DTORetornoPrediccion dtoRetornoPrediccion = DTORetornoPrediccion.builder()
                    .listaPrediccion(prediccionesParaPersistir)
                    .estrategiaGanadora(prediccionGanadora.get().getEstrategia())
                    .build();

            return dtoRetornoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

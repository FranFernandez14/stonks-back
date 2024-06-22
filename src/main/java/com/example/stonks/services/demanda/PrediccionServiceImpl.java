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
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@Service
public class PrediccionServiceImpl extends BaseServiceImpl<Prediccion, Long> implements PrediccionService {

    @Autowired
    private final PrediccionRepository prediccionRepository;

    @Autowired
    private final EntityManager entityManager;

    public PrediccionServiceImpl(
            BaseRepository<Prediccion, Long> baseRepository,
            PrediccionRepository prediccionRepository,
            EntityManager entityManager) {
        super(baseRepository);
        this.entityManager = entityManager;
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

            List<Prediccion> listaPrediccionRetorno = new ArrayList<Prediccion>();

            for (DTOPrediccion prediccion : prediccionGanadora.getListaPrediccion()) {
                //TODO: pisar la predicción previa si ya existía para ese periodo
                //Mostrar cuál predicción persistir
                Prediccion prediccionPersistible = Prediccion.builder()
                        .año(prediccion.getAño())
                        .mes(prediccion.getMes())
                        .cantidadPredecida(prediccion.getValorPrediccion())
                        .articulo(dtoIngresoParametrosDemanda.getArticulo())
                        .build();
                listaPrediccionRetorno.add(prediccionPersistible);
                this.prediccionRepository.save(prediccionPersistible);
            }

            List<OrdenDeCompra> ordenesPendientes = this.prediccionRepository.getOrdenDeCompraPendienteByArticulo(
                                                                dtoIngresoParametrosDemanda.getArticulo().getId());

            DetalleOrdenDeCompra detalleOrdenDeCompra = null;

            if (ordenesPendientes.isEmpty()) {

                Proveedor proveedor = dtoIngresoParametrosDemanda.getArticulo().getPredeterminado();
                Double costoEnvio = (double) proveedor.getCostoEnvio();

                List<ProveedorArticulo> proveedorArticulos = proveedor.getProveedorArticulos().stream()
                        .filter(proveedorArticulo -> proveedorArticulo.getArticulo().equals(dtoIngresoParametrosDemanda.getArticulo()))
                        .toList();
                if (proveedorArticulos.isEmpty()) throw new Exception("Revisar proveedor predeterminado, no ofrece el articulo deseado");

                Double costo = proveedorArticulos.get(0).getPrecio();

                int cantidadRequerida = (int) prediccionGanadora.getListaPrediccion()
                        .stream()
                        .mapToDouble(DTOPrediccion::getValorPrediccion)
                        .reduce(0, Double::sum);

                detalleOrdenDeCompra = DetalleOrdenDeCompra.builder()
                        .articulo(dtoIngresoParametrosDemanda.getArticulo())
                        .cantidad(cantidadRequerida)
                        .subTotal(cantidadRequerida * costo)
                        .build();


                OrdenDeCompra ordenDeCompra = OrdenDeCompra.builder()
                        .fechaCreacion(new Date(System.currentTimeMillis()))
                        .estadoActual(EstadoODC.SIN_CONFIRMAR)
                        .costoEnvio(costoEnvio)
                        .costoTotal(costo * cantidadRequerida)
                        .proveedor(proveedor)
                        .build();

                ordenDeCompra.getDetalles().add(detalleOrdenDeCompra);

                this.entityManager.persist(ordenDeCompra);
                this.entityManager.flush();
            }

            DTORetornoPrediccion dtoRetornoPrediccion = DTORetornoPrediccion.builder()
                    .listaPrediccion(listaPrediccionRetorno)
                    .detalleOrdenDeCompra(detalleOrdenDeCompra).build();

            return dtoRetornoPrediccion;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

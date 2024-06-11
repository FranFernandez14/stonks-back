package com.example.stonks.services.ventas;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.ventas.LineaVenta;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.ventas.VentaRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.orden_de_compra.OrdenDeCompraServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta, Long> implements VentaService{

    @Autowired
    private final VentaRepository ventaRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrdenDeCompraServiceImpl ordenDeCompraService;

    public VentaServiceImpl (BaseRepository<Venta, Long> baseRepository, VentaRepository ventaRepository){
        super(baseRepository);
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta registrarVenta(Venta v) throws Exception {

        for (LineaVenta lineaVenta : v.getLineasVenta()) { //por cada linea de venta
            Articulo articulo = lineaVenta.getArticulo();   //obtener el articulo
            //checkear si ya hay demanda para año-mes actual para ese articulo
            Long idArticulo = articulo.getId();
            Optional<Demanda> demandaMesAñoActual = ventaRepository.getDemandaMesAñoActual(idArticulo);

            if (demandaMesAñoActual.isEmpty()) {
                Demanda demanda = Demanda.builder()
                        .año(Calendar.getInstance().get(Calendar.YEAR))
                        .mes(Calendar.getInstance().get(Calendar.MONTH))
                        .articulo(articulo)
                        .cantidad(lineaVenta.getCantidad())
                        .build();

                entityManager.persist(demanda);
                entityManager.flush();
            } else {
                Demanda demanda = demandaMesAñoActual.get();
                demanda.setCantidad(demanda.getCantidad() + lineaVenta.getCantidad());
                entityManager.persist(demanda);
                entityManager.flush();
            }

            //generar orden de compra si el stock baja por debajo del punto de pedido
            articulo.setStockActual(articulo.getStockActual() - lineaVenta.getCantidad());
            if (articulo.getPuntoPedido() >= articulo.getStockActual()) {
                ordenDeCompraService.generarOrdenDeCompra(articulo.getId(), articulo.getPredeterminado().getId());
            }
        }
        Venta result = ventaRepository.save(v);
        return result;
    }
}

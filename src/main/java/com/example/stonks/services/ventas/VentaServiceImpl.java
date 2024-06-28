package com.example.stonks.services.ventas;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.ventas.LineaVenta;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.ventas.VentaRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
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
    private ArticuloServiceImpl articuloService;

    @Autowired
    private OrdenDeCompraServiceImpl ordenDeCompraService;

    public VentaServiceImpl(BaseRepository<Venta, Long> baseRepository, VentaRepository ventaRepository, EntityManager entityManager, ArticuloServiceImpl articuloService, OrdenDeCompraServiceImpl ordenDeCompraService) {
        super(baseRepository);
        this.ventaRepository = ventaRepository;
        this.entityManager = entityManager;
        this.articuloService = articuloService;
        this.ordenDeCompraService = ordenDeCompraService;
    }

    @Override
    public Venta registrarVenta(Venta v) throws Exception {

        try{

            for (LineaVenta lineaVenta : v.getLineasVenta()) { //por cada linea de venta
                Articulo articulo = articuloService.findById(lineaVenta.getArticulo().getId());   //obtener el articulo, se podría hacer desde el ventaRepository
                //checkear si hay stock
                if (articulo.getStockActual() < lineaVenta.getCantidad()){
                    throw new Exception("El artículo "+articulo.getNombre()+" no posee suficiente stock como para realizar la venta");
                }

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

                    entityManager.persist(demanda); //se podría persistir tod o de una
                    entityManager.flush();
                } else {
                    Demanda demanda = demandaMesAñoActual.get();
                    demanda.setCantidad(demanda.getCantidad() + lineaVenta.getCantidad());
                    entityManager.persist(demanda);
                    entityManager.flush();
                }

                //generar orden de compra si el stock baja por debajo del punto de pedido
                articulo.setStockActual(articulo.getStockActual() - lineaVenta.getCantidad());
                entityManager.persist(articulo);
                entityManager.flush();

                if (articulo.getFamiliaArticulo().getModeloInventario().equals(ModeloInventario.Lote_Fijo)) {
                    if (articulo.getPuntoPedido() >= articulo.getStockActual()) {
                        ordenDeCompraService.generarOrdenDeCompra(articulo.getId(), articulo.getPredeterminado().getId());
                    }
                }
            }
            return ventaRepository.save(v);

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
}

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
import com.example.stonks.services.demanda.DemandaServiceImpl;
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
    private DemandaServiceImpl demandaService;

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
                Articulo articulo = lineaVenta.getArticulo();
                System.out.println(articulo.getStockActual());//obtener el articulo, se podría hacer desde el ventaRepository
                //checkear si hay stock
                if (articulo.getStockActual() < lineaVenta.getCantidad()){
                    throw new Exception("El artículo "+articulo.getNombre()+" no posee suficiente stock como para realizar la venta");
                }

                //checkear si ya hay demanda para año-mes actual para ese articulo
                Long idArticulo = articulo.getId();

                articulo.setStockActual(articulo.getStockActual() - lineaVenta.getCantidad());

                articuloService.save(articulo);

                Optional<Demanda> demandaMesAñoActual = ventaRepository.getDemandaMesAñoActual(articulo);



                if (demandaMesAñoActual.isEmpty()) {

                    Demanda demanda = new Demanda();

                    demanda.setArticulo(articulo);
                    demanda.setAño(Calendar.getInstance().get(Calendar.YEAR));
                    demanda.setMes(Calendar.getInstance().get(Calendar.MONTH)+1);
                    demanda.setCantidad(lineaVenta.getCantidad());


                    System.out.println(demanda.getAño());
                    System.out.println(demanda.getMes());
                    demandaService.save(demanda);
                } else {
                    Demanda demanda = demandaMesAñoActual.get();
                    float cant = demanda.getCantidad();

                    float nuevaCant = cant + lineaVenta.getCantidad();

                    demanda.setCantidad(nuevaCant);

                    Demanda d = demandaService.update(demanda.getId(),demanda);
                }

                //generar orden de compra si el stock baja por debajo del punto de pedido

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

package com.example.stonks.services.articulos;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.repositories.articulos.ArticuloRepository;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.services.orden_de_compra.DetalleOrdenDeCompraServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo,Long> implements ArticuloService{

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private DemandaService demandaService;

    @Autowired
    private DetalleOrdenDeCompraServiceImpl detalleOrdenDeCompraService;

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository) {
        super(baseRepository);
    }

    public Optional<Integer> calcularLoteOptimo(Long idArticulo, int nroDemanda) {
        try {
            Articulo articulo = findById(idArticulo);

            // Selecciona la demanda específica basada en nroDemanda
            Optional<Demanda> demandaSeleccionada = articulo.getDemandas().stream()
                    .filter(d -> d.getNroDemanda() == nroDemanda)
                    .findFirst();

            if (demandaSeleccionada.isPresent()) {
                int cantidadDemanda = demandaSeleccionada.get().getCantidadDemanda();

                FamiliaArticulo familiaArticulo = articulo.getFamiliaArticulo();
                double loteOptimo;

                if (familiaArticulo.getModeloInventario() == ModeloInventario.Lote_Fijo) {
                    loteOptimo = Math.sqrt((2 * cantidadDemanda * articulo.getCp()) / articulo.getCa());

                } else {
                    throw new IllegalArgumentException("Modelo de inventario no soportado");
                }

                articulo.setLoteOptimo((int) loteOptimo);
                save(articulo);

                return Optional.of((int) loteOptimo);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Integer> calcularPuntoPedido(Long idArticulo, int nroDemanda) {
        try {
            Articulo articulo = findById(idArticulo);
            Optional<Demanda> demandaSeleccionada = demandaService.findByArticuloIdAndNroDemanda(idArticulo, nroDemanda);

            if (demandaSeleccionada.isPresent() && articulo.getPredeterminado() != null) {
                int cantidadDemanda = demandaSeleccionada.get().getCantidadDemanda();
                int diasDemoraEntrega = articulo.getPredeterminado().getDiasDemoraEntrega();
                int puntoPedido = cantidadDemanda * diasDemoraEntrega;

                articulo.setPuntoPedido(puntoPedido);
                save(articulo);

                return Optional.of(puntoPedido);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Integer> calcularStockSeguridad(Long idArticulo, double z, double desviacion) {
        try {
            Articulo articulo = findById(idArticulo);

            if (articulo.getPredeterminado() != null) {
                int diasDemoraEntrega = articulo.getPredeterminado().getDiasDemoraEntrega();
                int stockSeguridad = (int) Math.round(z * desviacion * Math.sqrt(diasDemoraEntrega));

                articulo.setStockSeguridad(stockSeguridad);
                save(articulo);

                return Optional.of(stockSeguridad);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        try {

            if (!baseRepository.existsById(id)) {
                throw new Exception("El artículo con ID " + id + " no existe.");
            }

            Articulo articulo = findById(id);

            boolean enOrdenDetallesEnProgreso = detalleOrdenDeCompraService.verificarArticuloEnOrdenesDetallesEnProgreso(articulo);

            if (enOrdenDetallesEnProgreso) {
                throw new Exception("No se puede eliminar el artículo porque está en una orden de compra en progreso.");
            }

            baseRepository.deleteById(id);
            return true;

        } catch (Exception e) {
            throw new Exception("Error al eliminar el artículo: " + e.getMessage());
        }
    }

}

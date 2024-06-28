package com.example.stonks.services.articulos;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.repositories.articulos.ArticuloRepository;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.services.demanda.DemandaService;
import com.example.stonks.services.orden_de_compra.DetalleOrdenDeCompraServiceImpl;
import com.example.stonks.services.orden_de_compra.OrdenDeCompraService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.stonks.repositories.DemandaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo,Long> implements ArticuloService{

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private DemandaService demandaService;

    @Autowired
    private OrdenDeCompraService ordenDeCompraService;

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository) {
        super(baseRepository);
    }

    @Transactional
    public Optional<Double> calcularCGI(Long idArticulo, int nroDemanda) {
        try {
            Articulo articulo = findById(idArticulo);

            // Selecciona la demanda específica basada en nroDemanda
            Optional<Demanda> demandaSeleccionada = articulo.getDemandas().stream()
                    .filter(d -> d.getNroDemanda() == nroDemanda)
                    .findFirst();

            if (demandaSeleccionada.isPresent()) {
                int cantidad = demandaSeleccionada.get().getCantidad();
                double precioVenta = articulo.getPrecioVenta();
                double ca = articulo.getCa();
                double cp = articulo.getCp();
                double loteOptimo = articulo.getLoteOptimo();

                // Calcula el CGI utilizando la fórmula proporcionada
                double cgi = (precioVenta * cantidad)
                        + (ca * (loteOptimo / 2))
                        + (cp * (cantidad / loteOptimo));

                return Optional.of(cgi);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Integer> calcularLoteOptimo(Long idArticulo, int nroDemanda) {
        try {
            Articulo articulo = findById(idArticulo);

            // Selecciona la demanda específica basada en nroDemanda
            Optional<Demanda> demandaSeleccionada = articulo.getDemandas().stream()
                    .filter(d -> d.getNroDemanda() == nroDemanda)
                    .findFirst();

            if (demandaSeleccionada.isPresent()) {
                int cantidadDemanda = demandaSeleccionada.get().getCantidad();

                FamiliaArticulo familiaArticulo = articulo.getFamiliaArticulo();
                double loteOptimo;

                if (familiaArticulo.getModeloInventario() == ModeloInventario.Lote_Fijo) {
                    loteOptimo = Math.sqrt((2 * cantidadDemanda * articulo.getCp()) / articulo.getCa());

                } else if (familiaArticulo.getModeloInventario() == ModeloInventario.Intervalo_Fijo) {
                    double k = articulo.getK();
                    loteOptimo = Math.sqrt((2 * cantidadDemanda * articulo.getCp() / articulo.getCa()) * (1 / (1 - cantidadDemanda / k)));
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
            // Selecciona la demanda específica basada en nroDemanda
            Optional<Demanda> demandaSeleccionada = articulo.getDemandas().stream()
                    .filter(d -> d.getNroDemanda() == nroDemanda)
                    .findFirst();

            if (demandaSeleccionada.isPresent() && articulo.getPredeterminado() != null) {
                int cantidadDemanda = demandaSeleccionada.get().getCantidad();
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

            // Verificar si hay órdenes de compra en estados que no permiten la eliminación
            List<EstadoODC> estadosPermitidos = Arrays.asList(
                    EstadoODC.SIN_CONFIRMAR,
                    EstadoODC.CONFIRMADA,
                    EstadoODC.ACEPTADA,
                    EstadoODC.EN_CAMINO
            );

            List<OrdenDeCompra> ordenes = ordenDeCompraService.getOrdenesByArticuloAndEstados(articulo.getId(), estadosPermitidos);

            if (!ordenes.isEmpty()) {
                throw new Exception("No se puede eliminar el artículo porque está asociado a una orden de compra en progreso.");
            }

            // Si no hay órdenes de compra que impidan la eliminación, proceder con la eliminación del artículo
            baseRepository.deleteById(id);
            return true;

        } catch (Exception e) {
            throw new Exception("Error al eliminar el artículo: " + e.getMessage());
        }
    }


    @Transactional
    public List<Articulo> listarProductosAReponer() {
        try {
            // Define los estados de las órdenes de compra que se consideran pendientes
            List<EstadoODC> estadosPendientes = Arrays.asList(
                    EstadoODC.SIN_CONFIRMAR,
                    EstadoODC.CONFIRMADA,
                    EstadoODC.ACEPTADA,
                    EstadoODC.EN_CAMINO
            );

            // Filtra los artículos que cumplen con los criterios
            List<Articulo> productosAReponer = articuloRepository.findAll().stream()
                    .filter(articulo -> articulo.getStockActual() <= articulo.getPuntoPedido())
                    .filter(articulo -> {
                        try {
                            // Obtener órdenes de compra asociadas al artículo en estados pendientes
                            List<OrdenDeCompra> ordenesPendientes = ordenDeCompraService.getOrdenesByArticuloAndEstados(articulo.getId(), estadosPendientes);

                            // Retorna true si no hay órdenes pendientes, es decir, el artículo no está en una orden pendiente
                            return ordenesPendientes.isEmpty();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false; // Si hay un error en la verificación, considera que está en progreso
                        }
                    })
                    .collect(Collectors.toList());

            return productosAReponer;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve una lista vacía en caso de error
        }
    }

    @Transactional
    public List<Articulo> listarProductosFaltantes() {
        try {
            // Filtra los artículos que cumplen con los criterios
            List<Articulo> productosFaltantes = articuloRepository.findAll().stream()
                    .filter(articulo -> articulo.getStockActual() <= articulo.getStockSeguridad())
                    .collect(Collectors.toList());

            return productosFaltantes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve una lista vacía en caso de error
        }
    }

}

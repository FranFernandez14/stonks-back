package com.example.stonks.services.orden_de_compra;

import com.example.stonks.dtos.orden_de_compra.CambiarProveedorDTO;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.entities.orden_de_compra.*;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.OrdenDeCompraRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrdenDeCompraServiceImpl extends BaseServiceImpl<OrdenDeCompra, Long> implements OrdenDeCompraService{
    public OrdenDeCompraServiceImpl(BaseRepository<OrdenDeCompra, Long> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;


    public Optional<OrdenDeCompra> getOrdenDeCompraPorProveedor(Proveedor proveedor, EstadoODC estado){
        return ordenDeCompraRepository.findOrdenDeCompraPorProveedorYPorEstado(proveedor, estado);
    }


    @Autowired
    private ArticuloServiceImpl articuloService;


    @Autowired
    private ProveedorArticuloServiceImpl proveedorArticuloService;

    @Autowired
    private DetalleOrdenDeCompraServiceImpl detalleOrdenDeCompraService;

    @Autowired
    private ProveedorServiceImpl proveedorService;

    public OrdenDeCompra generarOrdenDeCompra(Long idArticulo, Long idProveedor) throws Exception {

        Articulo articulo = articuloService.findById(idArticulo);

        List<EstadoODC> estados = Arrays.asList(
                EstadoODC.SIN_CONFIRMAR,
                EstadoODC.CONFIRMADA,
                EstadoODC.ACEPTADA,
                EstadoODC.EN_CAMINO
        );


        if (ordenDeCompraRepository.getOrderByArticuloAndEstados(articulo.getId(), estados).isEmpty()){

            Proveedor proveedor = proveedorService.findById(idProveedor);

            Optional <OrdenDeCompra> ordenDeCompra = getOrdenDeCompraPorProveedor(proveedor, EstadoODC.SIN_CONFIRMAR);
            Date date = new Date();
            ProveedorArticulo proveedorArticulo = proveedorArticuloService.getProveedorArticuloByProveedorAndArticulo(proveedor, articulo);

            DetalleOrdenDeCompra detalleOrdenDeCompra = new DetalleOrdenDeCompra();
            detalleOrdenDeCompra.setArticulo(articulo);
            detalleOrdenDeCompra.setCantidad(articulo.getLoteOptimo());
            detalleOrdenDeCompra.setValorArticulo(proveedorArticulo.getPrecio());
            detalleOrdenDeCompra.setTotalDetalle(detalleOrdenDeCompra.getCantidad()* detalleOrdenDeCompra.getValorArticulo());



            if (ordenDeCompra.isEmpty()){
                OrdenDeCompra ordenDeCompra1 = new OrdenDeCompra();
                ordenDeCompra1.setProveedor(proveedor);
                ordenDeCompra1.setCostoEnvio(proveedor.getCostoEnvio());
                ordenDeCompra1.setFechaCreacion(date);
                ordenDeCompra1.setCostoTotal(detalleOrdenDeCompra.getTotalDetalle());
                ordenDeCompra1.getDetalles().add(detalleOrdenDeCompra);
                ordenDeCompra1.setEstadoActual(EstadoODC.SIN_CONFIRMAR);
                return this.ordenDeCompraRepository.save(ordenDeCompra1);
            }else{
                OrdenDeCompra ordenDeCompra1 = ordenDeCompra.get();
                ordenDeCompra1.setCostoTotal(ordenDeCompra1.getCostoTotal() + detalleOrdenDeCompra.getTotalDetalle());
                ordenDeCompra1.getDetalles().add(detalleOrdenDeCompra);
                return this.ordenDeCompraRepository.save(ordenDeCompra1);
            }

        }else {
            return null;
        }
    }

    public void cambiarProveedor(CambiarProveedorDTO cambiarProveedorDTO) throws Exception {

        OrdenDeCompra ordenDeCompra1 = ordenDeCompraRepository.getReferenceById(cambiarProveedorDTO.getIdOrdenDeCompra());

        Optional<DetalleOrdenDeCompra> detalle = detalleOrdenDeCompraService.getDetalleByArticuloAndODC(cambiarProveedorDTO.getIdOrdenDeCompra(), cambiarProveedorDTO.getIdArticulo());

        ordenDeCompra1.getDetalles().remove(detalle);

        if (ordenDeCompra1.getDetalles().size()==0){
            ordenDeCompra1.setEstadoActual(EstadoODC.CANCELADA);
        }
        ordenDeCompraRepository.save(ordenDeCompra1);

        generarOrdenDeCompra(cambiarProveedorDTO.getIdArticulo(), cambiarProveedorDTO.getIdProveedor());


    }

    public void cambiarEstadoOrdenDeCompra(Long idOrdenDeCompra) throws Exception {

        OrdenDeCompra ordenDeCompra = ordenDeCompraRepository.getReferenceById(idOrdenDeCompra);

        EstadoODC estadoActual = ordenDeCompra.getEstadoActual();

        if (estadoActual == EstadoODC.SIN_CONFIRMAR) {
            ordenDeCompra.setEstadoActual(EstadoODC.CONFIRMADA);
        } else if (estadoActual == EstadoODC.CONFIRMADA) {
            ordenDeCompra.setEstadoActual(EstadoODC.ACEPTADA);
        } else if (estadoActual == EstadoODC.ACEPTADA) {
            ordenDeCompra.setEstadoActual(EstadoODC.EN_CAMINO);
        } else if (estadoActual == EstadoODC.EN_CAMINO) {
            ordenDeCompra.setEstadoActual(EstadoODC.RECIBIDA);
            for ( DetalleOrdenDeCompra detalle : ordenDeCompra.getDetalles()) {
                Articulo articulo = detalle.getArticulo();
                articulo.setStockActual(articulo.getStockActual() + (detalle.getCantidad()));
                articuloService.save(articulo);
            }
        }


        ordenDeCompraRepository.save(ordenDeCompra);
    }


    public void cancelarOrdenDeCompra (Long idOrdenDeCompra){

        OrdenDeCompra ordenDeCompra = ordenDeCompraRepository.getReferenceById(idOrdenDeCompra);

        ordenDeCompra.setEstadoActual(EstadoODC.CANCELADA);

        ordenDeCompraRepository.save(ordenDeCompra);


    }
    public Page<OrdenDeCompra> getByState(EstadoODC estadoODC, Pageable pageable) throws Exception {
        return ordenDeCompraRepository.getByState(estadoODC, pageable);
    }

    public void validarArticulosIntervaloFijo() throws Exception {

            List<Articulo> articulos = articuloService.findArticulosByModeloInventario(ModeloInventario.Intervalo_Fijo);

            Date hoy = new Date();

            for (Articulo articulo: articulos) {
                if (articulo.getUltimaFechaPedido() == null){
                    generarOrdenArticuloIntervaloFijo(articulo);
                }else {

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(articulo.getUltimaFechaPedido());
                    calendar.add(Calendar.DAY_OF_MONTH, articulo.getIntervaloPedido());
                    Date fechaLimite = calendar.getTime();

                    if (!fechaLimite.after(hoy)) {
                        generarOrdenArticuloIntervaloFijo(articulo);
                    }
                }
            }

    }

    public void generarOrdenArticuloIntervaloFijo(Articulo articulo) throws Exception {

        List<EstadoODC> estados = Arrays.asList(
                EstadoODC.SIN_CONFIRMAR,
                EstadoODC.CONFIRMADA,
                EstadoODC.ACEPTADA,
                EstadoODC.EN_CAMINO
        );


        if (ordenDeCompraRepository.getOrderByArticuloAndEstados(articulo.getId(), estados).isEmpty()) {


            int cantidadAPedir = articulo.getInventarioMaximo() - articulo.getStockActual();

            Proveedor proveedor = proveedorService.findById(articulo.getPredeterminado().getId());

            Optional<OrdenDeCompra> ordenDeCompra = getOrdenDeCompraPorProveedor(proveedor, EstadoODC.SIN_CONFIRMAR);
            Date date = new Date();
            ProveedorArticulo proveedorArticulo = proveedorArticuloService.getProveedorArticuloByProveedorAndArticulo(proveedor, articulo);

            DetalleOrdenDeCompra detalleOrdenDeCompra = new DetalleOrdenDeCompra();
            detalleOrdenDeCompra.setArticulo(articulo);
            detalleOrdenDeCompra.setCantidad(cantidadAPedir);
            detalleOrdenDeCompra.setValorArticulo(proveedorArticulo.getPrecio());
            detalleOrdenDeCompra.setTotalDetalle(detalleOrdenDeCompra.getCantidad() * detalleOrdenDeCompra.getValorArticulo());
            articulo.setUltimaFechaPedido(new Date());
            articuloService.save(articulo);

            if (ordenDeCompra.isEmpty()) {
                OrdenDeCompra ordenDeCompra1 = new OrdenDeCompra();
                ordenDeCompra1.setProveedor(proveedor);
                ordenDeCompra1.setCostoEnvio(proveedor.getCostoEnvio());
                ordenDeCompra1.setFechaCreacion(date);
                ordenDeCompra1.setCostoTotal(detalleOrdenDeCompra.getTotalDetalle());
                ordenDeCompra1.getDetalles().add(detalleOrdenDeCompra);
                ordenDeCompra1.setEstadoActual(EstadoODC.SIN_CONFIRMAR);
                System.out.println("hola");
                ordenDeCompraRepository.save(ordenDeCompra1);
            } else {
                OrdenDeCompra ordenDeCompra1 = ordenDeCompra.get();
                ordenDeCompra1.setCostoTotal(ordenDeCompra1.getCostoTotal() + detalleOrdenDeCompra.getTotalDetalle());
                ordenDeCompra1.getDetalles().add(detalleOrdenDeCompra);
                System.out.println("chau");
                ordenDeCompraRepository.save(ordenDeCompra1);
            }

        }



    }


    @Override
    public List<OrdenDeCompra> getOrdenesByArticuloAndEstados(Long articuloId, List<EstadoODC> estados) {
        return ordenDeCompraRepository.getOrderByArticuloAndEstados(articuloId, estados);
    }
}

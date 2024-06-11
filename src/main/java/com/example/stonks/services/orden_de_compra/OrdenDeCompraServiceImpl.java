package com.example.stonks.services.orden_de_compra;

import com.example.stonks.dtos.orden_de_compra.CambiarProveedorDTO;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.*;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.OrdenDeCompraRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrdenDeCompraServiceImpl extends BaseServiceImpl<OrdenDeCompra, Long> implements OrdenDeCompraService{
    public OrdenDeCompraServiceImpl(BaseRepository<OrdenDeCompra, Long> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    public OrdenDeCompra getOrdenDeCompraPorProveedor(Proveedor proveedor, EstadoODC estado){
        return ordenDeCompraRepository.findOrdenDeCompraPorProveedorYPorEstado(proveedor, estado);
    }


    @Autowired
    private ArticuloServiceImpl articuloService;


    @Autowired
    private ProveedorArticuloService proveedorArticuloService;

    @Autowired
    private DetalleOrdenDeCompraServiceImpl detalleOrdenDeCompraService;

    @Autowired
    private ProveedorServiceImpl proveedorService;

    public void generarOrdenDeCompra(Long idArticulo, Long idProveedor) throws Exception {

        Articulo articulo = articuloService.findById(idArticulo);

        Proveedor proveedor = proveedorService.findById(idProveedor);

        OrdenDeCompra ordenDeCompra = getOrdenDeCompraPorProveedor(proveedor, EstadoODC.SIN_CONFIRMAR);
        Date date = new Date();
        ProveedorArticulo proveedorArticulo = proveedorArticuloService.getProveedorArticuloByProveedorAndArticulo(proveedor, articulo);

        DetalleOrdenDeCompra detalleOrdenDeCompra = new DetalleOrdenDeCompra();
        detalleOrdenDeCompra.setArticulo(articulo);
        detalleOrdenDeCompra.setCantidad(articulo.getLoteOptimo());
        detalleOrdenDeCompra.setValorArticulo(proveedorArticulo.getPrecio());
        detalleOrdenDeCompra.setTotalDetalle(detalleOrdenDeCompra.getCantidad()* detalleOrdenDeCompra.getValorArticulo());



        if (ordenDeCompra == null){

            ordenDeCompra.setProveedor(proveedor);
            ordenDeCompra.setCostoEnvio(proveedor.getCostoEnvio());
            ordenDeCompra.setFechaCreacion(date);
            ordenDeCompra.setCostoTotal(detalleOrdenDeCompra.getTotalDetalle());
        }else{
            ordenDeCompra.setCostoTotal(ordenDeCompra.getCostoTotal() + detalleOrdenDeCompra.getTotalDetalle());
        }
        ordenDeCompra.getDetalles().add(detalleOrdenDeCompra);

        ordenDeCompraRepository.save(ordenDeCompra);

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

    public void cambiarEstadoOrdenDeCompra(Long idOrdenDeCompra){

        OrdenDeCompra ordenDeCompra = ordenDeCompraRepository.getReferenceById(idOrdenDeCompra);

        if (ordenDeCompra.getEstadoActual().getNumero()>1 && ordenDeCompra.getEstadoActual().getNumero()<5){
            ordenDeCompra.setEstadoActual(EstadoODC.fromNumero(ordenDeCompra.getEstadoActual().getNumero()+1));
        }
        ordenDeCompraRepository.save(ordenDeCompra);
    }

    public void cancelarOrdenDeCompra (Long idOrdenDeCompra){

        OrdenDeCompra ordenDeCompra = ordenDeCompraRepository.getReferenceById(idOrdenDeCompra);

        ordenDeCompra.setEstadoActual(EstadoODC.CANCELADA);

        ordenDeCompraRepository.save(ordenDeCompra);


    }


}

package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.*;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.DetalleOrdenDeCompraRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class DetalleOrdenDeCompraServiceImpl extends BaseServiceImpl<DetalleOrdenDeCompra, Long> implements DetalleOrdenDeCompraService {
    public DetalleOrdenDeCompraServiceImpl(BaseRepository<DetalleOrdenDeCompra, Long> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    private DetalleOrdenDeCompraRepository detalleOrdenDeCompraRepository;

    @Autowired
    private ArticuloServiceImpl articuloService;

    @Autowired
    private OrdenDeCompraService ordenDeCompraService;

    @Autowired
    private ProveedorArticuloService proveedorArticuloService;

    public void generarOrdenDeCompra(Long idArticulo) throws Exception {

        Articulo articulo = articuloService.findById(idArticulo);

        Proveedor proveedor = articulo.getPredeterminado();

        OrdenDeCompra ordenDeCompra = ordenDeCompraService.getOrdenDeCompraPorProveedor(proveedor, EstadoODC.SIN_CONFIRMAR);
        Date date = new Date();
        ProveedorArticulo proveedorArticulo = proveedorArticuloService.getProveedorArticuloByProveedorAndArticulo(proveedor, articulo);

        if (ordenDeCompra.equals(null)){

            DetalleOrdenDeCompra detalleOrdenDeCompra = new DetalleOrdenDeCompra();
            detalleOrdenDeCompra.setArticulo(articulo);
            detalleOrdenDeCompra.setCantidad(articulo.getLoteOptimo());
            detalleOrdenDeCompra.setValorArticulo(proveedorArticulo.getPrecio());
            detalleOrdenDeCompra.setTotalDetalle(detalleOrdenDeCompra.getCantidad()* detalleOrdenDeCompra.getValorArticulo());

            ordenDeCompra.setProveedor(proveedor);
            ordenDeCompra.setCostoEnvio(proveedor.getCostoEnvio());
            ordenDeCompra.setFechaCreacion(date);
            ordenDeCompra.setCostoTotal(detalleOrdenDeCompra.getTotalDetalle());

            detalleOrdenDeCompra.setOrdenDeCompra(ordenDeCompra);
            detalleOrdenDeCompraRepository.save(detalleOrdenDeCompra);
        }

    }
}

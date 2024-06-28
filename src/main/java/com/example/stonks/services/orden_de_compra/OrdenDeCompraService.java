package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface OrdenDeCompraService extends BaseService<OrdenDeCompra, Long> {


    public Optional<OrdenDeCompra> getOrdenDeCompraPorProveedor(Proveedor proveedor, EstadoODC estado);

    public List<OrdenDeCompra> getOrdenesByArticuloAndEstados(Long articuloId, List<EstadoODC> estados);

}

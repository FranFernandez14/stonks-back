package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.services.BaseService;

public interface OrdenDeCompraService extends BaseService<OrdenDeCompra, Long> {


    public OrdenDeCompra getOrdenDeCompraPorProveedor(Proveedor proveedor, EstadoODC estado);
}

package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.example.stonks.services.BaseService;

public interface ProveedorArticuloService extends BaseService<ProveedorArticulo, Long> {

    public ProveedorArticulo getProveedorArticuloByProveedorAndArticulo(Proveedor proveedor, Articulo articulo);

}



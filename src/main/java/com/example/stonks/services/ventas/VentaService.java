package com.example.stonks.services.ventas;

import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.services.BaseService;

public interface VentaService extends BaseService<Venta, Long> {

    Venta registrarVenta (Venta v) throws Exception;
}

package com.example.stonks.controllers.orden_de_compra;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.orden_de_compra.DetalleOrdenDeCompra;
import com.example.stonks.services.orden_de_compra.DetalleOrdenDeCompraServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/ordenesDeCompra/detalles")
public class DetalleOrdenDeCompraController extends BaseControllerImpl<DetalleOrdenDeCompra, DetalleOrdenDeCompraServiceImpl> {
}

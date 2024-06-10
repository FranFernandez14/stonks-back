package com.example.stonks.controllers.ventas;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.ventas.LineaVenta;
import com.example.stonks.services.ventas.LineaVentaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/lineas_venta")
public class LineaVentaController extends BaseControllerImpl<LineaVenta, LineaVentaServiceImpl> {
}

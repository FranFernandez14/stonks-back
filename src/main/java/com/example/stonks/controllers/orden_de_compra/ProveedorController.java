package com.example.stonks.controllers.orden_de_compra;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.services.orden_de_compra.ProveedorServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/proveedores")
public class ProveedorController extends BaseControllerImpl<Proveedor, ProveedorServiceImpl> {
}

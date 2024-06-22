package com.example.stonks.controllers.orden_de_compra;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.dtos.orden_de_compra.CambiarProveedorDTO;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.services.orden_de_compra.OrdenDeCompraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/ordenesDeCompra")
public class OrdenDeCompraController extends BaseControllerImpl<OrdenDeCompra, OrdenDeCompraServiceImpl> {

    @Autowired
    private OrdenDeCompraServiceImpl ordenDeCompraService;

    @PutMapping("/cambiarProveedor")
    public void cambiarProveedor(@RequestBody CambiarProveedorDTO cambiarProveedorDTO) throws Exception {
       ordenDeCompraService.cambiarProveedor(cambiarProveedorDTO);
    }

    @PutMapping("/cambiarEstado/{id}")
    public void cambiarEstado(@PathVariable Long id ){
        ordenDeCompraService.cambiarEstadoOrdenDeCompra(id);
    }

    @PutMapping("/cancelarOrdenDeCompra/{id}")
    public void cancelarOrdenDeCompra(@PathVariable Long id ){
        ordenDeCompraService.cancelarOrdenDeCompra(id);
    }


}

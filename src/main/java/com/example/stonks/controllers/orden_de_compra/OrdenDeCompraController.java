package com.example.stonks.controllers.orden_de_compra;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.dtos.orden_de_compra.CambiarProveedorDTO;
import com.example.stonks.dtos.orden_de_compra.GenerarOrdenDTO;
import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.services.orden_de_compra.OrdenDeCompraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/ordenesDeCompra")
@CrossOrigin(origins = "*")
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

    @PostMapping("/generarOrden")
    public void generarOrden(@RequestBody GenerarOrdenDTO generarOrdenDTO) throws Exception {
        ordenDeCompraService.generarOrdenDeCompra(generarOrdenDTO.getIdArticulo(), generarOrdenDTO.getIdProveedor());
    }

    @GetMapping("getByState/{idEstado}")
    public Page<OrdenDeCompra> getByState(@PathVariable Long idEstado, Pageable pageable) throws Exception{
        return ordenDeCompraService.getByState(EstadoODC.fromNumero(idEstado.intValue()), pageable);
    }

}

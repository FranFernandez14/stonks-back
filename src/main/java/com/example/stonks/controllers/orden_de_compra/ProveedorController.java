package com.example.stonks.controllers.orden_de_compra;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.dtos.orden_de_compra.ProveedorArticuloDTO;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.example.stonks.services.orden_de_compra.ProveedorArticuloService;
import com.example.stonks.services.orden_de_compra.ProveedorArticuloServiceImpl;
import com.example.stonks.services.orden_de_compra.ProveedorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/proveedores")
public class ProveedorController extends BaseControllerImpl<Proveedor, ProveedorServiceImpl> {

    @Autowired
    private ProveedorArticuloServiceImpl proveedorArticuloService;
    @GetMapping("/getProveedorArticulo/{idArticulo}")
    public List<ProveedorArticuloDTO>  getProveedorArticuloByArticulo(@PathVariable  Long idArticulo) {
        return proveedorArticuloService.getProveedorArticulobyArticulo(idArticulo);
    }
    @PutMapping("/alta{id}")
    public ResponseEntity<?> update(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(servicio.alta(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"");
        }
    }
}

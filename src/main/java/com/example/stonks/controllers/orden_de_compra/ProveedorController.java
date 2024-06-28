package com.example.stonks.controllers.orden_de_compra;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.services.orden_de_compra.ProveedorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/proveedores")
public class ProveedorController extends BaseControllerImpl<Proveedor, ProveedorServiceImpl> {

    @PutMapping("/alta{id}")
    public ResponseEntity<?> update(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(servicio.alta(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"");
        }
    }
}

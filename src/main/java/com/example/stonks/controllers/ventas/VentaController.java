package com.example.stonks.controllers.ventas;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.services.ventas.VentaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/ventas")
public class VentaController extends BaseControllerImpl<Venta, VentaServiceImpl> {

    @PostMapping("/registrar_venta")
    public ResponseEntity<?> registrarVenta(@RequestBody Venta venta){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.registrarVenta(venta));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}

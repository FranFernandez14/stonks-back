package com.example.stonks.controllers.ventas;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.services.ventas.ClienteServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/clientes")
public class ClienteController extends BaseControllerImpl<Cliente, ClienteServiceImpl> {

    @PutMapping("/alta{id}")
    public ResponseEntity<?> update(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(servicio.alta(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"");
        }
    }

}

package com.example.stonks.controllers;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.services.demanda.DemandaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/demanda")
public class DemandaController extends BaseControllerImpl<Demanda, DemandaServiceImpl> {


    @Override
    @PostMapping(path = "")
    public ResponseEntity<?> save(@RequestBody Demanda entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.save(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"" + e.getMessage() + "\"}");
        }

    }
}

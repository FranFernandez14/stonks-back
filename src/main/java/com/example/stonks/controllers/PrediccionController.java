package com.example.stonks.controllers;

import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.services.demanda.PrediccionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/prediccion")
public class PrediccionController extends BaseControllerImpl<Prediccion, PrediccionServiceImpl> {

    @PostMapping(path = "/predecir")
    public ResponseEntity<?> predecirDemanda(@RequestBody DTOIngresoParametrosDemanda entity){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(this.servicio.predecirDemanda(entity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"");
        }
    }
}

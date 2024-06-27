package com.example.stonks.controllers.articulos;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.services.articulos.FamiliaArticuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/familiaArticulo")
public class FamiliaArticuloController extends BaseControllerImpl<FamiliaArticulo, FamiliaArticuloServiceImpl> {

    @Autowired
    private FamiliaArticuloServiceImpl familiaArticuloService;


}

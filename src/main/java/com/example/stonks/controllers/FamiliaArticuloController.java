package com.example.stonks.controllers;

import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.services.FamiliaArticuloServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/familiaArticulo")
public class FamiliaArticuloController extends BaseControllerImpl<FamiliaArticulo, FamiliaArticuloServiceImpl>{

}

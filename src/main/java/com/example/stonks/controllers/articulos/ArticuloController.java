package com.example.stonks.controllers.articulos;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/articulos")
public class ArticuloController extends BaseControllerImpl<Articulo, ArticuloServiceImpl> {
}
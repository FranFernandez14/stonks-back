package com.example.stonks.controllers;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.services.DemandaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/demanda")
public class DemandaController extends BaseControllerImpl<Demanda, DemandaServiceImpl> {
}

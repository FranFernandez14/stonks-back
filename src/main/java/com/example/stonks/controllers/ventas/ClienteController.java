package com.example.stonks.controllers.ventas;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.services.ventas.ClienteServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/clientes")
public class ClienteController extends BaseControllerImpl<Cliente, ClienteServiceImpl> {
}

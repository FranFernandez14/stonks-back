package com.example.stonks.services.ventas;

import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.ventas.ClienteRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, Long> implements ClienteService{

    @Autowired
    private final ClienteRepository  clienteRepository;

    public ClienteServiceImpl (BaseRepository<Cliente, Long> baseRepository, ClienteRepository clienteRepository){
        super(baseRepository);
        this.clienteRepository = clienteRepository;
    }
}

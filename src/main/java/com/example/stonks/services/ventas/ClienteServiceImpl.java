package com.example.stonks.services.ventas;

import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.ventas.ClienteRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, Long> implements ClienteService{

    @Autowired
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl (BaseRepository<Cliente, Long> baseRepository, ClienteRepository clienteRepository){
        super(baseRepository);
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        try {
            if (clienteRepository.existsById(id)){
                Optional<Cliente> clienteOptional = clienteRepository.findById(id);
                Cliente cliente = clienteOptional.get();
                cliente.setFechaBaja(new Date());
                clienteRepository.save(cliente);
                return true;
            }else {
                throw new Exception("No se encontró el cliente con id "+id+" para dar de baja");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean alta(Long id) throws Exception {
        try {
            if (clienteRepository.existsById(id)){
                Optional<Cliente> clienteOptional = clienteRepository.findById(id);
                Cliente cliente = clienteOptional.get();
                if (cliente.getFechaBaja() == null){
                    throw new Exception("El cliente id:"+cliente.getId()+" ya se encuentra dado de alta");
                }
                cliente.setFechaBaja(null);
                clienteRepository.save(cliente);
                return true;
            }else {
                throw new Exception("No se encontró el cliente con id "+id+" para dar de alta");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}

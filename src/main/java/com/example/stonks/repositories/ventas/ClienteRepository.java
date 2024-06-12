package com.example.stonks.repositories.ventas;

import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente, Long> {
}

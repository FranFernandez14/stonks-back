package com.example.stonks.repositories.ventas;

import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends BaseRepository<Venta, Long> {
}

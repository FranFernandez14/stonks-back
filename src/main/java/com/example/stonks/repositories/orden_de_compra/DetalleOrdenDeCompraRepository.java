package com.example.stonks.repositories.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.DetalleOrdenDeCompra;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleOrdenDeCompraRepository extends BaseRepository<DetalleOrdenDeCompra, Long> {
}

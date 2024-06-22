package com.example.stonks.repositories.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends BaseRepository<Proveedor, Long> {
}

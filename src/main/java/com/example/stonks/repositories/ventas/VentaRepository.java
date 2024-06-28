package com.example.stonks.repositories.ventas;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.repositories.BaseRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface VentaRepository extends BaseRepository<Venta, Long> {
    @Query(value = "SELECT d.* FROM Demanda as d WHERE"
    +" d.mes = month(CURDATE()) AND d.año = year(CURDATE())"
    +" AND d.id_articulo = :idArticulo;", nativeQuery = true)
    Optional<Demanda> getDemandaMesAñoActual (@Param(value = "idArticulo") Long idArticulo);


}

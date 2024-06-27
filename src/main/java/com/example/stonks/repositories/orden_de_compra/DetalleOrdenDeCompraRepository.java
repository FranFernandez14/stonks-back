package com.example.stonks.repositories.orden_de_compra;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.DetalleOrdenDeCompra;
import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleOrdenDeCompraRepository extends BaseRepository<DetalleOrdenDeCompra, Long> {


    @Query(value = "SELECT * FROM detalle_orden_de_compra WHERE id_orden_de_compra = :ordenDeCompraId AND id_articulo = :articuloId", nativeQuery = true)
    Optional<DetalleOrdenDeCompra> findByOrdenDeCompraIdAndArticuloId(@Param("ordenDeCompraId") Long ordenDeCompraId, @Param("articuloId") Long articuloId);

}

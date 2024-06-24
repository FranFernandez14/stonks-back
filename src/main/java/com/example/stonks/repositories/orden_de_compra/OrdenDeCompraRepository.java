package com.example.stonks.repositories.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenDeCompraRepository extends BaseRepository<OrdenDeCompra, Long> {

    @Query("SELECT o FROM OrdenDeCompra o WHERE o.proveedor = :proveedor AND o.estadoActual = :estadoActual")
    OrdenDeCompra findOrdenDeCompraPorProveedorYPorEstado(@Param("proveedor") Proveedor proveedor, @Param("estadoActual") EstadoODC estadoActual);

    @Query("SELECT o FROM OrdenDeCompra o WHERE o.estadoActual = :estadoActual")
    Page<OrdenDeCompra> getByState(@Param("estadoActual") EstadoODC estadoODC, Pageable pageable);

}

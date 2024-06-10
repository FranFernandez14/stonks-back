package com.example.stonks.repositories.orden_de_compra;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorArticuloRepository extends BaseRepository<ProveedorArticulo, Long> {


    @Query("SELECT pa FROM ProveedorArticulo pa WHERE pa.proveedor = :proveedor AND pa.articulo = :articulo")
    ProveedorArticulo findProveedorArticuloByProveedorAndArticulo(@Param("proveedor") Proveedor proveedor, @Param("articulo") Articulo articulo);


}




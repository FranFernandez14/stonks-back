package com.example.stonks.repositories.orden_de_compra;

import com.example.stonks.dtos.orden_de_compra.ProveedorArticuloDTO;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ProveedorArticuloRepository extends BaseRepository<ProveedorArticulo, Long> {

    @Query(value = "SELECT pa FROM ProveedorArticulo as pa WHERE pa.proveedor_id = :id_proveedor AND pa.articulo_id = :id_articulo;", nativeQuery = true)
    ProveedorArticulo findProveedorArticuloByProveedorAndArticulo(@Param(value = "id_proveedor") Long id_proveedor, @Param(value = "id_articulo") Long id_articulo);

    @Query("SELECT new com.example.stonks.dtos.orden_de_compra.ProveedorArticuloDTO(p.id, p.nombre, pa.precio) " +
            "FROM Proveedor p JOIN p.proveedorArticulos pa " +
            "WHERE pa.articulo.id = :articuloId")
    List<ProveedorArticuloDTO> findProveedorArticuloDTOByArticuloId(@Param("articuloId") Long articuloId);


}




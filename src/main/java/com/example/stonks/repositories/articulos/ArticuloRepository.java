package com.example.stonks.repositories.articulos;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {

    @Query("SELECT a FROM Articulo a JOIN a.familiaArticulo f WHERE f.modeloInventario = :modeloInventario")
    List<Articulo> findArticulosByModeloInventario(@Param("modeloInventario") ModeloInventario modeloInventario);


}

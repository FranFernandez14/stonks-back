package com.example.stonks.repositories;

import com.example.stonks.entities.articulos.Articulo;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends BaseRepository <Articulo,Long>{
}

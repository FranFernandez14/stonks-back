package com.example.stonks.repositories.articulos;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.repositories.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {


}

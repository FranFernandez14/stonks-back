package com.example.stonks.repositories;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PrediccionRepository extends BaseRepository<Prediccion, Long> {

    @Query(
            value = "SELECT d.* FROM demanda as d" +
            "INNER JOIN articulo as a ON d.id_articulo = a.id " +
            "WHERE a.id = :articulo AND " +
            "(d.año < YEAR(CURDATE()) OR " +
            "(d.año = YEAR(CURDATE()) AND d.mes < MONTH(CURDATE())) " +
            "ORDER BY d.año DESC, d.mes DESC " +
            "LIMIT :periodos;",
            nativeQuery = true
    )
    public Collection<Demanda> getDemandas(@Param(value = "periodos") int periodos,
                                           @Param(value = "articulo") Long articulo);
}

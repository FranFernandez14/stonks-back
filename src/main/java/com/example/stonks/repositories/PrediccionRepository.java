package com.example.stonks.repositories;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PrediccionRepository extends BaseRepository<Prediccion, Long> {

    @Query(
            value = "SELECT d.* FROM demanda as d" +
            "INNER JOIN articulo as a ON d.id_articulo = a.id " +
            "WHERE a.id = :articulo AND " +
            "(d.año < YEAR(CURDATE()) OR " +
            "(d.año = YEAR(CURDATE()) AND d.mes < MONTH(CURDATE())) " +
            "ORDER BY d.año ASC, d.mes ASC " +
            "LIMIT :periodos;",
            nativeQuery = true
    )
    public ArrayList<Demanda> getDemandasAnteriores(@Param(value = "periodos") int periodos,
                                                    @Param(value = "articulo") Long articulo);

    @Query(value = "SELECT p.* FROM prediccion as p" +
            "INNER JOIN articulo as a ON p.id_articulo = a.id " +
            "WHERE a.id = :articulo AND " +
            "p.mes = :mes AND p.año = :año;",
            nativeQuery = true)
    public Prediccion getPrediccionByFecha (@Param(value = "id_articulo") Long id_articulo,
                                                       @Param(value = "mes") int mes,
                                                       @Param(value = "año") int año)
                                                        throws Exception;

//    @Query(value = "SELECT d.* FROM demanda as d " +
//            "WHERE d.año = :año AND d.mes = :mes AND id_articulo = :id_articulo;",
//            nativeQuery = true)
//    public Collection<Demanda> getDemandaByFecha (@Param(value = "id_articulo") Long id_articulo,
//                                                  @Param(value = "mes") int mes,
//                                                  @Param(value = "año") int año)
//                                                    throws Exception;

    @Query(value =  "SELECT d.* FROM demanda as d WHERE " +
                    "d.id_articulo = :id_articulo AND " +
                    "d.año <= :año_hasta AND d.año >= :año_desde " +
                    "ORDER BY d.año DESC, d.mes DESC;",
                    nativeQuery = true)
    public ArrayList<Demanda> getDemandasAnterioresEstacionalidad (@Param(value = "periodos") int periodos,
                                                                   @Param(value = "id_articulo") Long id_articulo,
                                                                   @Param(value = "año_desde") int año_desde,
                                                                   @Param(value = "año_hasta") int año_hasta)
                                                                    throws Exception;
}

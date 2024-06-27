package com.example.stonks.repositories;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOPromedioDemanda;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrediccionRepository extends BaseRepository<Prediccion, Long> {

    @Query(value = "SELECT d FROM Demanda d " +
            "WHERE d.articulo = :articulo AND " +
            "d.fechaBaja IS NULL AND " +
            "(d.año < YEAR(CURRENT_DATE) OR " +
            "(d.año = YEAR(CURRENT_DATE) AND d.mes < MONTH(CURRENT_DATE))) " +
            "ORDER BY d.año DESC, d.mes DESC")
    public ArrayList<Demanda> getDemandasAnteriores(@Param(value = "articulo") Articulo articulo,
                                                    Limit limit);

    @Query(value = "SELECT p.* FROM prediccion as p " +
            "INNER JOIN articulo as a ON p.id_articulo = a.id " +
            "WHERE a.id = :id_articulo AND " +
            "p.fecha_baja IS NULL AND " +
            "p.mes = :mes AND p.año = :año;",
            nativeQuery = true)
    public Optional<Prediccion> getPrediccionByFecha(@Param(value = "id_articulo") Long id_articulo,
                                                     @Param(value = "mes") int mes,
                                                     @Param(value = "año") int año);

    @Query(value =  "SELECT new com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOPromedioDemanda(AVG(d.cantidad), d.mes) " +
                    "FROM Demanda d WHERE " +
                    "d.articulo.id = :id_articulo AND " +
                    "d.fechaBaja IS NULL AND " +
                    "d.año BETWEEN :año_desde AND :año_hasta " +
                    "GROUP BY d.mes " +
                    "ORDER BY d.mes DESC")
    public ArrayList<DTOPromedioDemanda> getDemandasPromediosPorMes(@Param(value = "id_articulo") Long id_articulo,
                                                                    @Param(value = "año_desde") int año_desde,
                                                                    @Param(value = "año_hasta") int año_hasta);

    @Query(value = "SELECT d FROM Demanda d WHERE d.año = :año AND d.fechaBaja IS NULL AND d.articulo.id = :id_articulo ORDER BY d.mes DESC")
    public ArrayList<Demanda> getDemandasByAño(@Param(value = "id_articulo") Long id_articulo,
                                               @Param(value = "año") int año);

    //Retorna una lista de predicciones cuyo mes y año se encuentre en la lista
    ArrayList<Prediccion> findByMesInAndAñoInAndArticulo(List<Integer> meses, List<Integer> años, Articulo articulo);
}

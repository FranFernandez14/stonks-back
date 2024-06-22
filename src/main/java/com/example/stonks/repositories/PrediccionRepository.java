package com.example.stonks.repositories;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOPromedioDemanda;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PrediccionRepository extends BaseRepository<Prediccion, Long> {

    @Query(
            value = "SELECT d.* FROM demanda as d " +
            "INNER JOIN articulo as a ON d.id_articulo = a.id " +
            "WHERE a.id = :articulo AND " +
            "(d.año < YEAR(CURDATE()) OR " +
            "(d.año = YEAR(CURDATE()) AND d.mes < MONTH(CURDATE()))) " +
            "ORDER BY d.año DESC, d.mes DESC " +
            "LIMIT :periodos;",
            nativeQuery = true
    )
    public ArrayList<Demanda> getDemandasAnteriores(@Param(value = "periodos") int periodos,
                                                    @Param(value = "articulo") Long articulo)
                                                    throws Exception;

    /*Por como lo hemos hecho, podriamos guardar mas de una prediccion para un mismo periodo
    en el caso que predizcamos en dos meses distintos para 3 meses futuros. Habrian predicciones duplicadas
    por lo tanto se limita la prediccion buscada a la primera que se encuentre.
    */
    @Query(value = "SELECT p.* FROM prediccion as p " +
            "INNER JOIN articulo as a ON p.id_articulo = a.id " +
            "WHERE a.id = :id_articulo AND " +
            "p.mes = :mes AND p.año = :año LIMIT 1;",
            nativeQuery = true)
    public Prediccion getPrediccionByFecha(@Param(value = "id_articulo") Long id_articulo,
                                           @Param(value = "mes") int mes,
                                           @Param(value = "año") int año)
                                            throws Exception;

    @Query(value =  "SELECT new com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOPromedioDemanda(AVG(d.cantidad), d.mes) " +
                    "FROM Demanda d WHERE " +
                    "d.articulo.id = :id_articulo AND " +
                    "d.año BETWEEN :año_desde AND :año_hasta " +
                    "GROUP BY d.mes " +
                    "ORDER BY d.mes DESC")
    public ArrayList<DTOPromedioDemanda> getDemandasPromediosPorMes(@Param(value = "id_articulo") Long id_articulo,
                                                                    @Param(value = "año_desde") int año_desde,
                                                                    @Param(value = "año_hasta") int año_hasta)
                                                                    throws Exception;

    @Query(value = "SELECT d.* FROM demanda as d WHERE d.año = :año AND d.id_articulo = :id_articulo " +
                    "ORDER BY d.mes DESC;",
            nativeQuery = true)
    public ArrayList<Demanda> getDemandasByAño(@Param(value = "id_articulo") Long id_articulo,
                                               @Param(value = "año") int año) throws Exception;

    @Query(value =  "SELECT o.* FROM orden_de_compra as o " +
                    "INNER JOIN detalle_orden_de_compra as d ON d.id_orden_de_compra = o.id " +
                    "WHERE d.id_articulo = :id_articulo AND o.estado_actual = 1;",
                    nativeQuery = true)
    public ArrayList<OrdenDeCompra> getOrdenDeCompraPendienteByArticulo(@Param(value = "id_articulo") Long id_articulo) throws Exception;
}

package com.example.stonks.repositories;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.services.BaseService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface DemandaRepository extends BaseRepository<Demanda, Long> {

    @Query(value = "SELECT SUM(lv.cantidad) FROM linea_venta as lv " +
            "INNER JOIN venta as v ON lv.id_venta=v.id " +
            "WHERE lv.id_articulo = :id_articulo AND YEAR(v.fecha_venta) = :año AND MONTH(v.fecha_venta) = :mes " +
            "GROUP BY lv.id_articulo;",
            nativeQuery = true)
    public int listVentasPorArticulo (@Param(value = "id_articulo") Long id_articulo,
                                   @Param(value = "mes") int mes,
                                   @Param(value = "año") int año)
                                    throws Exception;

    @Query(value = "SELECT d.* FROM demanda as d " +
                    "WHERE d.año = :año AND d.mes = :mes AND id_articulo = :id_articulo;",
            nativeQuery = true)
    public ArrayList<Demanda> getDemandaByFecha (@Param(value = "id_articulo") Long id_articulo,
                                                 @Param(value = "mes") int mes,
                                                 @Param(value = "año") int año)
                                                    throws Exception;


}

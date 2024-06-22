package com.example.stonks.services.demanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.entities.orden_de_compra.DetalleOrdenDeCompra;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DTORetornoPrediccion {
    private List<Prediccion> listaPrediccion;
    private DetalleOrdenDeCompra detalleOrdenDeCompra;
}

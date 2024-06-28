package com.example.stonks.services.demanda;

import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.entities.orden_de_compra.DetalleOrdenDeCompra;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTORetornoPrediccion {
    private List<Prediccion> listaPrediccion;
    private String estrategiaGanadora;
}

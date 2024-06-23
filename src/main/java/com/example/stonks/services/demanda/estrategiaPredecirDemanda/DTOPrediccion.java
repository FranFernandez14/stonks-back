package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DTOPrediccion {

    private float valorPrediccion;

    private int mes;

    private int año;

}

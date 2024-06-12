package com.example.stonks.services.estrategiaPredecirDemanda;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTOPrediccion {

    private float valorPrediccion;

    private float errorCometido;

}

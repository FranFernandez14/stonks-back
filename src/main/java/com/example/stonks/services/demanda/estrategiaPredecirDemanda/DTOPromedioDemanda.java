package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOPromedioDemanda {

    private Double promedioMes;

    private int mes;
}

package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import java.util.ArrayList;
import java.util.List;

public class FactoriaEstrategiaPredecirDemanda {

    private static FactoriaEstrategiaPredecirDemanda instance;

    //SINGLETON
    public static FactoriaEstrategiaPredecirDemanda getInstance() {
        if (FactoriaEstrategiaPredecirDemanda.instance == null) {
            FactoriaEstrategiaPredecirDemanda.instance = new FactoriaEstrategiaPredecirDemanda();
            return FactoriaEstrategiaPredecirDemanda.instance;
        } else {
            return FactoriaEstrategiaPredecirDemanda.instance;
        }
    }

    public List<EstrategiaPredecirDemanda> obtenerEstrategias() {

        List <EstrategiaPredecirDemanda> lista = new ArrayList<EstrategiaPredecirDemanda>();

        lista.add(new EstrategiaMetodoIndices());
        lista.add(new EstrategiaPromedioMovil());
        lista.add(new EstrategiaPMSuavizado());

        return lista;
    }
}

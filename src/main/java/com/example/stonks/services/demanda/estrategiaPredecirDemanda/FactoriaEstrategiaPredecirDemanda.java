package com.example.stonks.services.demanda.estrategiaPredecirDemanda;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FactoriaEstrategiaPredecirDemanda {

    @Autowired
    private List<EstrategiaPredecirDemanda> listaEstrategias = new ArrayList<EstrategiaPredecirDemanda>();

    private List<EstrategiaPredecirDemanda> listaRetorno = new ArrayList<EstrategiaPredecirDemanda>();

    @PostConstruct
    private void inicializarFactoria() {
        listaRetorno.addAll(this.listaEstrategias);
    }

    public List<EstrategiaPredecirDemanda> obtenerEstrategias(){
        return listaRetorno;
    }
}

package com.example.stonks.services.ventas;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.ventas.LineaVenta;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.ventas.VentaRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta, Long> implements VentaService{

    @Autowired
    private final VentaRepository ventaRepository;

    public VentaServiceImpl (BaseRepository<Venta, Long> baseRepository, VentaRepository ventaRepository){
        super(baseRepository);
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta registrarVenta(Venta v) throws Exception {

        for (LineaVenta lineaVenta : v.getLineasVenta()) { //por cada linea de venta
            Articulo articulo = lineaVenta.getArticulo();   //obtener el articulo
            //checkear si ya hay demanda para año-mes actual para ese articulo

            //si ya la hay, añadir la cantidad de la linea de venta, a la demanda, y persistir

            //si no la hay, crear una nueva "Demanda" de este artículo, con año-mes actual, y la cantidad de la linea de venta
            //persistir
        }
        //persistir venta, y por cascadeo, sus lineas de venta
        return ; //retornar la venta persistida
    }
}

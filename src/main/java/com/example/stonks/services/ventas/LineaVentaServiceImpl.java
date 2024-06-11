package com.example.stonks.services.ventas;

import com.example.stonks.entities.ventas.LineaVenta;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.ventas.LineaVentaRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineaVentaServiceImpl extends BaseServiceImpl<LineaVenta, Long> implements LineaVentaService{

    @Autowired
    private final LineaVentaRepository lineaVentaRepository;

    public LineaVentaServiceImpl (BaseRepository<LineaVenta, Long> baseRepository, LineaVentaRepository lineaVentaRepository){
        super(baseRepository);
        this.lineaVentaRepository = lineaVentaRepository;
    }
}

package com.example.stonks.services;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.DemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandaServiceImpl extends BaseServiceImpl<Demanda, Long> implements DemandaService {

    @Autowired
    private final DemandaRepository demandaRepository;

    public DemandaServiceImpl(
            BaseRepository<Demanda, Long> baseRepository,
            DemandaRepository demandaRepository) {
        super(baseRepository);
        this.demandaRepository = demandaRepository;
    }

    @Override
    public Demanda save(Demanda entity) throws Exception {
        try {
            int cantidadVentas = this.demandaRepository.listVentasPorArticulo(entity.getArticulo().getId(),
                                                                            entity.getMes(),
                                                                            entity.getAño());
            entity.setCantidad(cantidadVentas);
            this.baseRepository.save(entity);
        } catch (Exception e) {
            throw new Exception();
        }
    }
}

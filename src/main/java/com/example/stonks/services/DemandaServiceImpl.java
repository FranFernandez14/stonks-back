package com.example.stonks.services;

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
}

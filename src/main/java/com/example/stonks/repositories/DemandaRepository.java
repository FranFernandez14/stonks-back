package com.example.stonks.repositories;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.services.BaseService;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandaRepository extends BaseRepository<Demanda, Long> {
}

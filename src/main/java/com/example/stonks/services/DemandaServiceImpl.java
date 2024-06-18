package com.example.stonks.services;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.demanda.Prediccion;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.DemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
            int año = entity.getAño();
            int mes = entity.getMes();
            Long idArticulo = entity.getArticulo().getId();

            Collection<Demanda> demandaPrevia = this.demandaRepository.getDemandaByFecha(idArticulo, mes, año);

            //Revisa si ya existía antes una demanda previa, si ya existía no se persiste
            if(demandaPrevia.isEmpty()) {
                int cantidadVentas = this.demandaRepository.listVentasPorArticulo(entity.getArticulo().getId(),
                        entity.getMes(),
                        entity.getAño());
                entity.setCantidad(cantidadVentas);
                return this.baseRepository.save(entity);
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new Exception();
        }
    }
}

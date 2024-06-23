package com.example.stonks.services.demanda;

import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.DemandaRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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

            int cantidadVentas = this.demandaRepository.listVentasPorArticulo(
                    idArticulo,
                    mes,
                    año);

            entity.setCantidad(cantidadVentas);

            //Revisa si ya existía antes una demanda previa
            ArrayList<Demanda> demandaPrevia = this.demandaRepository.getDemandaByFecha(idArticulo, mes, año);
            if(!(demandaPrevia.isEmpty())) {
                demandaPrevia.get(0).setCantidad(cantidadVentas);
                return this.demandaRepository.save(demandaPrevia.get(0));
            } else {
                Demanda demanda = Demanda.builder()
                        .mes(mes)
                        .año(año)
                        .cantidad(cantidadVentas)
                        .articulo(entity.getArticulo())
                        .build();
                return this.demandaRepository.save(demanda);
            }

        } catch (Exception e) {
            throw new Exception();
        }
    }
}

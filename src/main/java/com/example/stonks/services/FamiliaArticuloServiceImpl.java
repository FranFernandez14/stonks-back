package com.example.stonks.services;

import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.FamiliaArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamiliaArticuloServiceImpl extends BaseServiceImpl<FamiliaArticulo,Long> implements FamiliaArticuloService{

    @Autowired
    private FamiliaArticuloRepository familiaArticuloRepository;

    public FamiliaArticuloServiceImpl(BaseRepository<FamiliaArticulo,Long> baseRepository) {super(baseRepository);}
}

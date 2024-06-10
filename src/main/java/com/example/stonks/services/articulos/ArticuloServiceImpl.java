package com.example.stonks.services.articulos;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.repositories.articulos.ArticuloRepository;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo,Long> implements ArticuloService{

    @Autowired
    private ArticuloRepository articuloRepository;

    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository) {super(baseRepository);}
}

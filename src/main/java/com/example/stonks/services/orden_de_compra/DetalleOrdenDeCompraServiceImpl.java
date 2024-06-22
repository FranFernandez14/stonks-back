package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.*;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.DetalleOrdenDeCompraRepository;
import com.example.stonks.services.BaseServiceImpl;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DetalleOrdenDeCompraServiceImpl extends BaseServiceImpl<DetalleOrdenDeCompra, Long> implements DetalleOrdenDeCompraService {
    public DetalleOrdenDeCompraServiceImpl(BaseRepository<DetalleOrdenDeCompra, Long> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    private DetalleOrdenDeCompraRepository detalleOrdenDeCompraRepository;

    public Optional<DetalleOrdenDeCompra> getDetalleByArticuloAndODC(Long idOrdenDeCompra, Long idArticulo){
        return  detalleOrdenDeCompraRepository.findByOrdenDeCompraIdAndArticuloId(idOrdenDeCompra, idArticulo);
    }

}

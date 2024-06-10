package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.EstadoODC;
import com.example.stonks.entities.orden_de_compra.OrdenDeCompra;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.OrdenDeCompraRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenDeCompraServiceImpl extends BaseServiceImpl<OrdenDeCompra, Long> implements OrdenDeCompraService{
    public OrdenDeCompraServiceImpl(BaseRepository<OrdenDeCompra, Long> baseRepository) {
        super(baseRepository);
    }

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    public OrdenDeCompra getOrdenDeCompraPorProveedor(Proveedor proveedor, EstadoODC estado){
        return ordenDeCompraRepository.findOrdenDeCompraPorProveedorYPorEstado(proveedor, estado);
    }

}

package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.orden_de_compra.ProveedorArticulo;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.ProveedorArticuloRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorArticuloServiceImpl extends BaseServiceImpl<ProveedorArticulo, Long> implements ProveedorArticuloService {

   @Autowired
   private ProveedorArticuloRepository proveedorArticuloRepository;
    public ProveedorArticuloServiceImpl(BaseRepository<ProveedorArticulo, Long> baseRepository, ProveedorArticuloRepository proveedorArticuloRepository) {
        super(baseRepository);
        this.proveedorArticuloRepository = proveedorArticuloRepository;
    }


    public ProveedorArticulo getProveedorArticuloByProveedorAndArticulo(Proveedor proveedor, Articulo articulo){
        return proveedorArticuloRepository.findProveedorArticuloByProveedorAndArticulo(proveedor.getId(), articulo.getId());
    }
}

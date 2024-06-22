package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.ProveedorRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServiceImpl extends BaseServiceImpl<Proveedor, Long> implements ProveedorService{

    @Autowired
    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl (BaseRepository<Proveedor, Long> baseRepository, ProveedorRepository proveedorRepository){
        super(baseRepository);
        this.proveedorRepository = proveedorRepository;
    }
}

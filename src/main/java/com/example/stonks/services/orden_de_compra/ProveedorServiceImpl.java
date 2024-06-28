package com.example.stonks.services.orden_de_compra;

import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.orden_de_compra.ProveedorRepository;
import com.example.stonks.services.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProveedorServiceImpl extends BaseServiceImpl<Proveedor, Long> implements ProveedorService{

    @Autowired
    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl (BaseRepository<Proveedor, Long> baseRepository, ProveedorRepository proveedorRepository){
        super(baseRepository);
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        try {
            if (proveedorRepository.existsById(id)){
                Optional<Proveedor> proveedorOptional = proveedorRepository.findById(id);
                Proveedor proveedor = proveedorOptional.get();
                proveedor.setFechaBaja(new Date());
                proveedorRepository.save(proveedor);
                return true;
            }else {
                throw new Exception("No se encontró el proveedor con id "+id+" para dar de baja");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean alta(Long id) throws Exception {
        try {
            if (proveedorRepository.existsById(id)){
                Optional<Proveedor> proveedorOptional = proveedorRepository.findById(id);
                Proveedor proveedor = proveedorOptional.get();
                if (proveedor.getFechaBaja() == null){
                    throw new Exception("El proveedor id:"+proveedor.getId()+" ya se encuentra dado de alta");
                }
                proveedor.setFechaBaja(null);
                proveedorRepository.save(proveedor);
                return true;
            }else {
                throw new Exception("No se encontró el proveedor con id "+id+" para dar de alta");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}

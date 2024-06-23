package com.example.stonks.services.articulos;

import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.repositories.BaseRepository;
import com.example.stonks.repositories.articulos.FamiliaArticuloRepository;
import com.example.stonks.services.BaseServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamiliaArticuloServiceImpl extends BaseServiceImpl<FamiliaArticulo,Long> implements FamiliaArticuloService{

    @Autowired
    private FamiliaArticuloRepository familiaArticuloRepository;

    public FamiliaArticuloServiceImpl(BaseRepository<FamiliaArticulo,Long> baseRepository) {super(baseRepository);}

    @Transactional
    public FamiliaArticulo save(FamiliaArticulo familiaArticulo) throws Exception {
        // Validación de los parámetros
        if (familiaArticulo.getNombre() == null || familiaArticulo.getNombre().isEmpty()) {
            throw new Exception("El nombre de la familia de artículos es requerido.");
        }

        if (familiaArticulo.getModeloInventario() == null) {
            throw new Exception("El tipo de inventario es requerido.");
        }

        // Persistir la entidad utilizando el método save de la superclase
        return super.save(familiaArticulo);
    }

    // Método específico para la creación de FamiliaArticulo
    public FamiliaArticulo crearFamiliaArticulo(String nombre, ModeloInventario modeloInventario) throws Exception {
        FamiliaArticulo nuevaFamilia = new FamiliaArticulo();
        nuevaFamilia.setNombre(nombre);
        nuevaFamilia.setModeloInventario(modeloInventario);

        // Utilizar el método sobrescrito save
        return save(nuevaFamilia);
    }

}

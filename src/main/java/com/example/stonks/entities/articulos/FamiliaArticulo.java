package com.example.stonks.entities.articulos;

import com.example.stonks.entities.Base;
import jakarta.persistence.Entity;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class FamiliaArticulo extends Base {

    private int cod;
    private String nombre;
    private Date fechaBaja;
    private ModeloInventario modeloInventario;

}

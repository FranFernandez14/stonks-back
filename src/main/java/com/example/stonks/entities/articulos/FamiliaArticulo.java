package com.example.stonks.entities.articulos;

import com.example.stonks.entities.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Date;

@Data
@Entity
@Table(name = "FamiliaArticulo")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class FamiliaArticulo extends Base {

    @Column(name = "cod")
    private int cod;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fechaBaja")
    private Date fechaBaja;

    private ModeloInventario modeloInventario;

}

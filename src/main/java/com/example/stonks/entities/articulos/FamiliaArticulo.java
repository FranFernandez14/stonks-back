package com.example.stonks.entities.articulos;

import com.example.stonks.entities.Base;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private ModeloInventario modeloInventario;

}

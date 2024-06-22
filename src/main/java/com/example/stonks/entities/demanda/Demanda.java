package com.example.stonks.entities.demanda;

import com.example.stonks.entities.Base;
import com.example.stonks.entities.articulos.Articulo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter

public class Demanda extends Base {

    @Column(name = "nro_demanda")
    private int nroDemanda;
    @Column(name = "cantidad_demanda")
    private int cantidadDemanda;
    @Column(name = "mes")
    private int mes;
    @Column(name = "año")
    private int año;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;



}

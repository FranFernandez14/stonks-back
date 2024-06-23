package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Cliente")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends Base {

    @Column(name = "nro_cliente")
    private int nroCliente;

    @Column(name = "nombre_cliente")
    private String nombre;

    @Column(name = "cuit_cliente")
    private String CUIT;
}

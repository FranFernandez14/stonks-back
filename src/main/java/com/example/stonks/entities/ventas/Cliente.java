package com.example.stonks.entities.ventas;

import com.example.stonks.entities.Base;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Cliente extends Base {

    private int nroCliente;
    private String nombre;
    private String CUIT;
}

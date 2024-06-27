package com.example.stonks.dtos.orden_de_compra;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProveedorArticuloDTO {

    private Long idProveedor;
    private String nombreProveedor;
    private Double precio;

}

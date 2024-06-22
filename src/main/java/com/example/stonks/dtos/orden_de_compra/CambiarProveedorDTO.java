package com.example.stonks.dtos.orden_de_compra;

import lombok.Data;

@Data
public class CambiarProveedorDTO {

    private Long idArticulo;
    private Long idProveedor;
    private Long idOrdenDeCompra;
}

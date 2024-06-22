package com.example.stonks.controllers.articulos;

import com.example.stonks.controllers.BaseControllerImpl;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.services.articulos.ArticuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/articulos")
public class ArticuloController extends BaseControllerImpl<Articulo, ArticuloServiceImpl> {

    @Autowired
    private ArticuloServiceImpl articuloService;

    @PostMapping("/{id}/calcular-lote-optimo")
    public ResponseEntity<?> calcularLoteOptimo(@PathVariable Long id, @RequestParam int nroDemanda) {
        try {
            Optional<Integer> loteOptimo = articuloService.calcularLoteOptimo(id, nroDemanda);
            if (loteOptimo.isPresent()) {
                return ResponseEntity.ok(loteOptimo.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda no encontrada para el artículo.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al calcular el Lote Óptimo.");
        }
    }

    @PostMapping("/{id}/calcular-punto-pedido")
    public ResponseEntity<?> calcularPuntoPedido(@PathVariable Long id, @RequestParam int nroDemanda) {
        try {
            Optional<Integer> puntoPedido = articuloService.calcularPuntoPedido(id, nroDemanda);
            if (puntoPedido.isPresent()) {
                return ResponseEntity.ok(puntoPedido.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo calcular el Punto de Pedido.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al calcular el Punto de Pedido.");
        }
    }

    @PostMapping("/{id}/calcular-stock-seguridad")
    public ResponseEntity<?> calcularStockSeguridad(@PathVariable Long id, @RequestParam double z, @RequestParam double desviacion) {
        try {
            Optional<Integer> stockSeguridad = articuloService.calcularStockSeguridad(id, z, desviacion);
            if (stockSeguridad.isPresent()) {
                return ResponseEntity.ok(stockSeguridad.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo calcular el Stock de Seguridad.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al calcular el Stock de Seguridad.");
        }
    }

}
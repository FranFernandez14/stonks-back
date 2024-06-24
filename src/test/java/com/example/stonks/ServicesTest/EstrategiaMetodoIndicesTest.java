package com.example.stonks.ServicesTest;

import com.example.stonks.StonksApplication;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.articulos.FamiliaArticulo;
import com.example.stonks.entities.articulos.ModeloInventario;
import com.example.stonks.entities.demanda.Demanda;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.repositories.PrediccionRepository;
import com.example.stonks.services.demanda.DTOIngresoParametrosDemanda;
import com.example.stonks.services.demanda.DTORetornoPrediccion;
import com.example.stonks.services.demanda.PrediccionServiceImpl;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.DTOListaPrediccion;
import com.example.stonks.services.demanda.estrategiaPredecirDemanda.EstrategiaMetodoIndices;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = StonksApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(
        properties = {"spring.datasource.url = jdbc:h2:mem:test",
                "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect"}
)
@Transactional
public class EstrategiaMetodoIndicesTest {

    @Autowired
    private PrediccionServiceImpl prediccionService;

    @Autowired
    private PrediccionRepository prediccionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testEstrategiaMetodoIndices() {

        FamiliaArticulo familiaArticulo = new FamiliaArticulo(1, "Lentes", new Date(24), ModeloInventario.Lote_Fijo);
        entityManager.persist(familiaArticulo);
        entityManager.flush();

        Proveedor proveedor = Proveedor.builder()
                .cod(1)
                .nombre("Optica Marcos")
                .email("opticamarcos@gmail.com")
                .diasDemoraEntrega(3)
                .costoEnvio(500)
                .build();

        entityManager.persist(proveedor);
        entityManager.flush();

        Articulo articulo = Articulo.builder()
                .cod("art1")
                .nombre("Lentes Locos")
                .stockActual(100)
                .stockSeguridad(30)
                .loteOptimo(70)
                .puntoPedido(50)
                .precioVenta(40)
                .inventarioMaximo(300)
                .costoAlmacenamiento(540)
                .familiaArticulo(familiaArticulo)
                .predeterminado(proveedor)
                .build();

        entityManager.persist(articulo);
        entityManager.flush();

        //cantidad de las demandas, es igual al ejemplo desarrollado en excel para poder verificar resultados

        int[] demandas = {80,75,85,90,110,105,100,90,85,77,75,80,
                80,70,80,90,113,110,100,88,85,77,75,82,
                85,85,93,95,125,115,102,102,90,78,82,78,
                105,85,82,115,131,120,113,110,95,85,83,80 };

        int k = 0;

        //Creación de demandas históricas de los últimos 3 años
        for (int año = 0; año<4; año++){
            for (int mes=1; mes<13; mes++){
                Demanda demanda = Demanda.builder()
                        .articulo(articulo)
                        .mes(mes)
                        .año(2020+año)
                        .cantidad(demandas[k])
                        .build();
                entityManager.persist(demanda);
                entityManager.flush();
                k++;
            }
        }

        //Input para el método de la estrategia
        DTOIngresoParametrosDemanda dtoIngresoParametrosDemanda = DTOIngresoParametrosDemanda.builder()
                .articulo(articulo)
                .cantidadPeriodosAPredecir(1)
                .cantidadPeriodosParaError(1)
                .demandaAñoAPredecir(1250)
                .ciclos(3)
                .build();

        int añoHastaPrediccion = Calendar.getInstance().get(Calendar.YEAR) - 1;
        int añoHastaParaError = añoHastaPrediccion - 1;

        /*try {
            when(prediccionRepository.getDemandasPromediosPorMes( dtoIngresoParametrosDemanda.getArticulo().getId(), (añoHastaParaError-dtoIngresoParametrosDemanda.getCiclos()), añoHastaParaError )).thenReturn(TODO poner cositas);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }*/

        DTORetornoPrediccion resultado = new DTORetornoPrediccion();

        try {
            resultado = prediccionService.predecirDemanda(dtoIngresoParametrosDemanda);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

        float errorCometido = 67.67821782f;
        System.out.println("Error cometido: "+errorCometido);

        assertEquals(1, resultado.getListaPrediccion().size()); //TODO repensar las afirmaciones desde el punto de vista del service, no la estrategia
        //assertEquals(errorCometido, resultado.getListaPrediccion().get(0).get);
        assertEquals(99.73404255f, resultado.getListaPrediccion().get(0).getCantidadPredecida());

    }
}

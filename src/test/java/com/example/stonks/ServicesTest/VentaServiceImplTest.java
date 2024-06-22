package com.example.stonks.ServicesTest;
import com.example.stonks.StonksApplication;
import com.example.stonks.entities.articulos.Articulo;
import com.example.stonks.entities.orden_de_compra.Proveedor;
import com.example.stonks.entities.ventas.Cliente;
import com.example.stonks.entities.ventas.LineaVenta;
import com.example.stonks.entities.ventas.Venta;
import com.example.stonks.repositories.ventas.VentaRepository;
import com.example.stonks.services.ventas.VentaService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StonksApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(
        properties = {"spring.datasource.url = jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect"}
)
@Transactional
public class VentaServiceImplTest {

    @Autowired
    private VentaService ventaService;

    @MockBean
    private VentaRepository ventaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testRegistrarCompra() throws Exception{

        Cliente cliente = Cliente.builder()
                .nroCliente(1)
                .CUIT("1234")
                .nombre("Juan Perez")
                .build();

        entityManager.persist(cliente);
        entityManager.flush();

        Proveedor proveedor1 = Proveedor.builder()
                .cod(1)
                .nombre("Nivea Oficial")
                .build();

        entityManager.persist(proveedor1);
        entityManager.flush();

        Articulo articulo1 = Articulo.builder()
                .cod("1")
                .nombre("Jabon Nivea")
                .precioVenta(50)
                .stockActual(100)
                .predeterminado(proveedor1)
                .stockSeguridad(20)
                .puntoPedido(50)
                .build();

        entityManager.persist(articulo1);
        entityManager.flush();

        Proveedor proveedor2 = Proveedor.builder()
                .cod(2)
                .nombre("Optica Marcos")
                .build();

        entityManager.persist(proveedor2);
        entityManager.flush();

        Articulo articulo2 = Articulo.builder()
                .cod("2")
                .nombre("Lentes Ratón")
                .precioVenta(100)
                .stockActual(200)
                .predeterminado(proveedor2)
                .stockSeguridad(50)
                .puntoPedido(100)
                .build();

        entityManager.persist(articulo2);
        entityManager.flush();

        List<LineaVenta> lineasVenta = new ArrayList<>();

        LineaVenta lineaVenta1 =LineaVenta.builder()
                .contadorLineaVenta(1)
                .articulo(articulo1)
                .precioUnitario(articulo1.getPrecioVenta())
                .cantidad(20)
                .build();

        LineaVenta lineaVenta2 =LineaVenta.builder()
                .contadorLineaVenta(2)
                .articulo(articulo2)
                .precioUnitario(articulo2.getPrecioVenta())
                .cantidad(50)
                .build();

        lineasVenta.add(lineaVenta1);
        lineasVenta.add(lineaVenta2);

        Venta venta = Venta.builder()
                .fechaVenta(new Date())
                .nroVenta(1)
                .cliente(cliente)
                .lineasVenta(lineasVenta)
                .build();

        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta ventaRegistrada = ventaService.registrarVenta(venta);
        assertEquals(ventaRegistrada.getLineasVenta().get(0).getArticulo().getNombre(), "Jabon Nivea");
        assertEquals(ventaRegistrada.getLineasVenta().get(0).getArticulo().getPredeterminado().getNombre(), "Nivea Oficial");

        assertEquals(ventaRegistrada.getLineasVenta().get(1).getArticulo().getNombre(), "Lentes Ratón");
        assertEquals(ventaRegistrada.getLineasVenta().get(1).getArticulo().getPredeterminado().getNombre(), "Optica Marcos");
    }
}

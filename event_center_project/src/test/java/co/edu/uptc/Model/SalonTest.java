package co.edu.uptc.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SalonTest {

    @Test
    void testConstructorVacio() {
        Salon salon = new Salon();

        assertAll("Salón vacío",
            () -> assertEquals(0, salon.getId(), "id debe ser 0"),
            () -> assertNull(salon.getSalonName(), "salonName debe ser null"),
            () -> assertEquals(0, salon.getCapacity(), "capacity debe ser 0"),
            () -> assertEquals(0.0, salon.getPriceByHour(), "priceByHour debe ser 0.0"),
            () -> assertEquals(0, salon.getNumberOfReservations(), "numberOfReservations debe ser 0")
        );
    }

    @Test
    void testConstructorConParametros() {
        Salon salon = new Salon(1, "Salón Principal", 100, 50.0);

        assertAll("Constructor con parámetros",
            () -> assertEquals(1, salon.getId()),
            () -> assertEquals("Salón Principal", salon.getSalonName()),
            () -> assertEquals(100, salon.getCapacity()),
            () -> assertEquals(50.0, salon.getPriceByHour()),
            () -> assertEquals(0, salon.getNumberOfReservations()) // Se inicializa a 0
        );
    }

    @Test
    void testGetCapacity() {
        Salon salon = new Salon(1, "Salón A", 50, 30.0);
        assertEquals(50, salon.getCapacity());
    }

    @Test
    void testGetId() {
        Salon salon = new Salon(5, "Salón B", 75, 40.0);
        assertEquals(5, salon.getId());
    }

    @Test
    void testGetNumberOfReservations() {
        Salon salon = new Salon(2, "Salón C", 80, 35.0);
        salon.setNumberOfReservations(10);
        assertEquals(10, salon.getNumberOfReservations());
    }

    @Test
    void testGetPriceByHour() {
        Salon salon = new Salon(3, "Salón D", 60, 45.5);
        assertEquals(45.5, salon.getPriceByHour());
    }

    @Test
    void testGetSalonName() {
        Salon salon = new Salon(4, "Salón Elegante", 90, 55.0);
        assertEquals("Salón Elegante", salon.getSalonName());
    }

    @Test
    void testSetCapacity() {
        Salon salon = new Salon();
        salon.setCapacity(120);
        assertEquals(120, salon.getCapacity());
    }

    @Test
    void testSetId() {
        Salon salon = new Salon();
        salon.setId(99);
        assertEquals(99, salon.getId());
    }

    @Test
    void testSetNumberOfReservations() {
        Salon salon = new Salon();
        salon.setNumberOfReservations(25);
        assertEquals(25, salon.getNumberOfReservations());
    }

    @Test
    void testSetPriceByHour() {
        Salon salon = new Salon();
        salon.setPriceByHour(75.75);
        assertEquals(75.75, salon.getPriceByHour());
    }

    @Test
    void testSetSalonName() {
        Salon salon = new Salon();
        salon.setSalonName("Salón VIP");
        assertEquals("Salón VIP", salon.getSalonName());
    }
}

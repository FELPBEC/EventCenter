package co.edu.uptc.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

public class FiltrerServiceTest {

    private FiltrerService filtrerService;
    private List<Salon> salons;
    private List<Booking> bookings;
    private Client client;

    @BeforeEach
    void setUp() {
        LocalDateTime filtroInicio = LocalDateTime.of(2026, 8, 25, 12, 0, 0);
        filtrerService = new FiltrerService(filtroInicio, 2);

        salons = new ArrayList<>();
        salons.add(new Salon(1, "Afrodita", 200, 230000.0));
        salons.add(new Salon(2, "Zeus", 12, 120000.0));
        salons.add(new Salon(3, "Apolo", 80, 150000.0));

        client = new Client("pepe", 1234, "1234", "1234", "julian", true);

        bookings = new ArrayList<>();
        bookings.add(new Booking(1, client, salons.get(0), "2026/08/25/11:00:00", 2, "2026/08/25/13:00:00"));
        bookings.get(0).setPrice(460000.0);
    }

    @Test
    void testFilterByDate() {
        List<Salon> available = filtrerService.filterByDate(salons, bookings);

        assertEquals(2, available.size());
        assertTrue(available.stream().anyMatch(salon -> salon.getId() == 2));
        assertTrue(available.stream().anyMatch(salon -> salon.getId() == 3));
        assertFalse(available.stream().anyMatch(salon -> salon.getId() == 1));
    }

    @Test
    void testFilterByPrice() {
        List<Salon> priceFiltered = filtrerService.filterByPrice(260000.0, salons);

        assertEquals(1, priceFiltered.size());
        assertTrue(priceFiltered.stream().anyMatch(salon -> salon.getId() == 2));
        assertFalse(priceFiltered.stream().anyMatch(salon -> salon.getId() == 1));
        assertFalse(priceFiltered.stream().anyMatch(salon -> salon.getId() == 3));
    }

    @Test
    void testFiltrerByCapacity() {
        List<Salon> capacityFiltered = filtrerService.filtrerByCapacity(100, salons);

        assertEquals(1, capacityFiltered.size());
        assertTrue(capacityFiltered.stream().anyMatch(salon -> salon.getId() == 1));
        assertFalse(capacityFiltered.stream().anyMatch(salon -> salon.getId() == 2));
        assertFalse(capacityFiltered.stream().anyMatch(salon -> salon.getId() == 3));
    }

    @Test
    void testGetStartDate() {
        LocalDateTime expected = LocalDateTime.of(2026, 8, 25, 12, 0, 0);
        assertEquals(expected, filtrerService.getStartDate());
    }

    @Test
    void testIsFiltrerByCapacity() {
        assertFalse(filtrerService.isFiltrerByCapacity());
        filtrerService.setFiltrerByCapacity(true);
        assertTrue(filtrerService.isFiltrerByCapacity());
    }

    @Test
    void testIsFiltrerByPrice() {
        assertFalse(filtrerService.isFiltrerByPrice());
        filtrerService.setFiltrerByPrice(true);
        assertTrue(filtrerService.isFiltrerByPrice());
    }

    @Test
    void testSendFiltrerSalonList() {
        filtrerService.setFiltrerByPrice(true);
        filtrerService.setFiltrerByCapacity(true);

        List<Salon> result = filtrerService.sendFiltrerSalonList(500000.0, 80, salons, bookings);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getId());
    }

    @Test
    void testSetFiltrerByCapacity() {
        filtrerService.setFiltrerByCapacity(true);
        assertTrue(filtrerService.isFiltrerByCapacity());

        filtrerService.setFiltrerByCapacity(false);
        assertFalse(filtrerService.isFiltrerByCapacity());
    }

    @Test
    void testSetFiltrerByPrice() {
        filtrerService.setFiltrerByPrice(true);
        assertTrue(filtrerService.isFiltrerByPrice());

        filtrerService.setFiltrerByPrice(false);
        assertFalse(filtrerService.isFiltrerByPrice());
    }
}

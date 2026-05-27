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
import co.edu.uptc.Services.BookingServices;
import co.edu.uptc.Services.SalonServices;

public class FiltrerServiceTest {

    private FiltrerService filtrerService;
    private List<Salon> salons;
    private List<Booking> bookings;
    private Client client;

    @BeforeEach
    void setUp() {
        LocalDateTime filtroInicio = LocalDateTime.of(2026, 8, 25, 12, 0, 0);

        salons = new ArrayList<>();
        salons.add(new Salon(1, "Afrodita", 200, 230000.0));
        salons.add(new Salon(2, "Zeus", 12, 120000.0));
        salons.add(new Salon(3, "Apolo", 80, 150000.0));

        client = new Client("pepe", 1234, "1234", "3142159789", "julian.moreno21@gmail.com", true);
        
        bookings = new ArrayList<>();
        // CRÍTICO: Agregamos una reserva para Afrodita exactamente en la fecha del filtro
        // Esto hará que el filtro de disponibilidad la descarte, dejando solo a Apolo (ID 3).
        bookings.add(new Booking(1, client, salons.get(0), "2026/08/25/12:00:00", 2, "2026/08/25/14:00:00"));

        SalonServices mockSalonServices = new SalonServices(salons);
        BookingServices mockBookingServices = new BookingServices(bookings);

        filtrerService = new FiltrerService(filtroInicio, 2, mockSalonServices, mockBookingServices);
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

        List<Salon> result = filtrerService.sendFiltrerSalonList(500000.0, 80);

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
}
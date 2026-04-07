package co.edu.uptc.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

public class RankedSalonTest {

    private RankedSalon rankedSalon;
    private List<Salon> salons;
    private List<Booking> bookings;
    private Client client;

    @BeforeEach
    void setUp() {
        rankedSalon = new RankedSalon();
        salons = new ArrayList<>();
        salons.add(new Salon(1, "Afrodita", 200, 230000.0));
        salons.add(new Salon(2, "Zeus", 12, 120000.0));
        salons.add(new Salon(3, "Apolo", 80, 150000.0));
        salons.add(new Salon(4, "Hera", 100, 180000.0));
        salons.add(new Salon(5, "Poseidon", 150, 210000.0));
        salons.add(new Salon(6, "Ares", 50, 90000.0));

        client = new Client("pepe", 1234, "1234", "3142159789", "julian", true);
        bookings = new ArrayList<>();

        bookings.add(new Booking(1, client, salons.get(0), "2026/08/25/12:00:00", 2, "2026/08/25/14:00:00"));
        bookings.add(new Booking(2, client, salons.get(0), "2026/08/26/12:00:00", 2, "2026/08/26/14:00:00"));
        bookings.add(new Booking(3, client, salons.get(1), "2026/08/25/12:00:00", 2, "2026/08/25/14:00:00"));
        bookings.add(new Booking(4, client, salons.get(2), "2026/08/27/12:00:00", 2, "2026/08/27/14:00:00"));
        bookings.add(new Booking(5, client, salons.get(2), "2026/08/28/12:00:00", 2, "2026/08/28/14:00:00"));
        bookings.add(new Booking(6, client, salons.get(3), "2026/08/29/12:00:00", 2, "2026/08/29/14:00:00"));
        bookings.add(new Booking(7, client, salons.get(3), "2026/08/30/12:00:00", 2, "2026/08/30/14:00:00"));
        bookings.add(new Booking(8, client, salons.get(3), "2026/08/31/12:00:00", 2, "2026/08/31/14:00:00"));
        bookings.add(new Booking(9, client, salons.get(4), "2026/09/01/12:00:00", 2, "2026/09/01/14:00:00"));

        bookings.forEach(booking -> booking.setPrice(booking.getSalon().getPriceByHour() * booking.getAmountOfHours()));
    }

    @Test
    void testSetNumberOfReservations() {
        rankedSalon.setNumberOfReservations(salons, bookings);

        assertEquals(2, salons.get(0).getNumberOfReservations());
        assertEquals(1, salons.get(1).getNumberOfReservations());
        assertEquals(2, salons.get(2).getNumberOfReservations());
        assertEquals(3, salons.get(3).getNumberOfReservations());
        assertEquals(1, salons.get(4).getNumberOfReservations());
        assertEquals(0, salons.get(5).getNumberOfReservations());
    }

    @Test
    void testSendTop5BestSalons() {
        rankedSalon.setNumberOfReservations(salons, bookings);

        List<Salon> top5 = rankedSalon.sendTop5BestSalons(salons);

        assertEquals(5, top5.size());
        assertEquals(4, top5.get(0).getId());
        assertEquals(1, top5.get(1).getId());
        assertEquals(3, top5.get(2).getId());
        assertEquals(2, top5.get(3).getId());
        assertEquals(5, top5.get(4).getId());

        assertFalse(top5.stream().anyMatch(salon -> salon.getId() == 6));
    }
}

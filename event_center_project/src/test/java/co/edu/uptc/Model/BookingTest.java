package co.edu.uptc.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class BookingTest {
    
    @Test
    void testConstructorVacio() {
        Booking booking = new Booking();

        assertEquals(0, booking.getId());
        assertNull(booking.getClient());
        assertNull(booking.getSalon());
        assertNull(booking.getStartDate());
        assertEquals(0, booking.getAmountOfHours());
        assertNull(booking.getEndDate());
        assertEquals(0.0, booking.getPrice());
    }

    @Test
    void testConstructorConParametros() {
        Client client = new Client(); 
        Salon salon = new Salon();

        Booking booking = new Booking(
                1,
                client,
                salon,
                "2026-04-06 10:00",
                2,
                "2026-04-06 12:00"
        );

        assertEquals(1, booking.getId());
        assertEquals(client, booking.getClient());
        assertEquals(salon, booking.getSalon());
        assertEquals("2026-04-06 10:00", booking.getStartDate());
        assertEquals(2, booking.getAmountOfHours());
        assertEquals("2026-04-06 12:00", booking.getEndDate());
    }

    @Test
    void testSettersYGetters() {
        Booking booking = new Booking();

        Client client = new Client();
        Salon salon = new Salon();

        booking.setId(10);
        booking.setClient(client);
        booking.setSalon(salon);
        booking.setStartDate("2026-04-07 08:00");
        booking.setAmountOfHours(3);
        booking.setEndDate("2026-04-07 11:00");
        booking.setPrice(150.0);

        assertEquals(10, booking.getId());
        assertEquals(client, booking.getClient());
        assertEquals(salon, booking.getSalon());
        assertEquals("2026-04-07 08:00", booking.getStartDate());
        assertEquals(3, booking.getAmountOfHours());
        assertEquals("2026-04-07 11:00", booking.getEndDate());
        assertEquals(150.0, booking.getPrice());

    }
}



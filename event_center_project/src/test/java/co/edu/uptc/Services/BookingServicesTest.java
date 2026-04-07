package co.edu.uptc.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

public class BookingServicesTest {

    private BookingServices bookingServices;
    private List<Booking> bookingList;
    private Client clientA;
    private Client clientB;
    private Salon salonA;
    private Salon salonB;

    @BeforeEach
    void setUp() {
        bookingServices = new BookingServices();
        bookingList = new ArrayList<>();

        clientA = new Client("ClienteA", 1, "passA", "3000000000", "a@test.com", true);
        clientB = new Client("ClienteB", 2, "passB", "3000000001", "b@test.com", false);

        salonA = new Salon(1, "SalonA", 100, 50.0);
        salonB = new Salon(2, "SalonB", 200, 100.0);

        Booking booking1 = new Booking(1, clientA, salonA, "2026/04/01/10:00:00", 2, "2026/04/01/12:00:00");
        booking1.setPrice(100.0);

        Booking booking2 = new Booking(2, clientA, salonA, "2026/04/01/14:00:00", 3, "2026/04/01/17:00:00");
        booking2.setPrice(150.0);

        Booking booking3 = new Booking(3, clientB, salonB, "2026/04/02/09:00:00", 1, "2026/04/02/10:00:00");
        booking3.setPrice(100.0);

        Booking booking4 = new Booking(4, clientA, salonA, "2026/04/03/11:00:00", 4, "2026/04/03/15:00:00");
        booking4.setPrice(200.0);

        bookingList.add(booking1);
        bookingList.add(booking2);
        bookingList.add(booking3);
        bookingList.add(booking4);
    }

    @Test
    void testSaveNewBooking() {
        Booking newBooking = new Booking(5, clientB, salonA, "2026/04/04/10:00:00", 2, "2026/04/04/12:00:00");
        newBooking.setPrice(120.0);

        bookingServices.saveNewBooking(newBooking, bookingList);

        assertEquals(5, bookingList.size());
        Booking saved = bookingServices.sendBookingById(5, bookingList);
        assertNotNull(saved);
        assertEquals(5, saved.getId());
        assertEquals("ClienteB", saved.getClient().getUserName());
    }

    @Test
    void testSendBookingById() {
        Booking booking = bookingServices.sendBookingById(2, bookingList);

        assertNotNull(booking);
        assertEquals(2, booking.getId());
        assertEquals("ClienteA", booking.getClient().getUserName());

        assertNull(bookingServices.sendBookingById(99, bookingList));
    }

    @Test
    void testSendBookingByClientId() {
        Booking booking = bookingServices.sendBookingByClientId(1, bookingList);

        assertNotNull(booking);
        assertEquals(1, booking.getClient().getId());
        assertEquals("ClienteA", booking.getClient().getUserName());

        assertNull(bookingServices.sendBookingByClientId(99, bookingList));
    }

    @Test
    void testSendBookingBySalonId() {
        Booking booking = bookingServices.sendBookingBySalonId(2, bookingList);

        assertNotNull(booking);
        assertEquals(2, booking.getSalon().getId());
        assertEquals("SalonB", booking.getSalon().getSalonName());

        assertNull(bookingServices.sendBookingBySalonId(99, bookingList));
    }

    @Test
    void testFoundBookingById() {
        assertTrue(bookingServices.foundBookingById(1, bookingList));
        assertFalse(bookingServices.foundBookingById(99, bookingList));
    }

    @Test
    void testSendBookingPosition() {
        assertEquals(1, bookingServices.sendBookingPosition(2, bookingList));
        assertEquals(bookingList.size() + 1, bookingServices.sendBookingPosition(99, bookingList));
    }

    @Test
    void testCancelBooking() {
        bookingServices.cancelBooking(2, bookingList);

        assertFalse(bookingServices.foundBookingById(2, bookingList));
        assertEquals(3, bookingList.size());
    }

    @Test
    void testUpdateBooking() {
        Booking updatedBooking = new Booking(2, clientB, salonB, "2026/04/05/08:00:00", 5, "2026/04/05/13:00:00");
        updatedBooking.setPrice(500.0);

        boolean result = bookingServices.updateBooking(2, updatedBooking, bookingList);

        assertTrue(result);
        Booking booking = bookingServices.sendBookingById(2, bookingList);
        assertNotNull(booking);
        assertEquals("ClienteB", booking.getClient().getUserName());
        assertEquals("SalonB", booking.getSalon().getSalonName());
        assertEquals(500.0, booking.getPrice());

        boolean notFoundResult = bookingServices.updateBooking(99, updatedBooking, bookingList);
        assertFalse(notFoundResult);
    }

    @Test
    void testSendNewId() {
        assertEquals(5, bookingServices.sendNewId(bookingList));

        bookingList.add(new Booking(10, clientB, salonB, "2026/04/06/10:00:00", 2, "2026/04/06/12:00:00"));
        assertEquals(11, bookingServices.sendNewId(bookingList));
    }

    @Test
    void testSendBookingListByClient() {
        List<Booking> clientBookings = bookingServices.sendBookingListByClient(1, bookingList);

        assertEquals(3, clientBookings.size());
        assertTrue(clientBookings.stream().allMatch(b -> b.getClient().getId() == 1));
    }

    @Test
    void testSendBookingListBySalon() {
        List<Booking> salonBookings = bookingServices.sendBookingListBySalon(1, bookingList);

        assertEquals(3, salonBookings.size());
        assertTrue(salonBookings.stream().allMatch(b -> b.getSalon().getId() == 1));
    }

    @Test
    void testCalculatePriceBooking() {
        Booking booking = new Booking(5, clientA, salonA, "2026/04/06/08:00:00", 8, "2026/04/06/16:00:00");

        List<Booking> clientBookings = new ArrayList<>();
        clientBookings.add(new Booking(6, clientA, salonA, "2026/04/05/10:00:00", 1, "2026/04/05/11:00:00"));
        clientBookings.add(new Booking(7, clientA, salonA, "2026/04/05/12:00:00", 1, "2026/04/05/13:00:00"));
        clientBookings.add(new Booking(8, clientA, salonA, "2026/04/05/14:00:00", 1, "2026/04/05/15:00:00"));
        clientBookings.add(booking);

        double price = bookingServices.calculatePriceBooking(booking, clientBookings);

        assertEquals(244.8, price, 0.001);
        assertEquals(244.8, booking.getPrice(), 0.001);
    }

    @Test
    void testObtenerReservasPorRango() {
        List<Booking> filtered = bookingServices.obtenerReservasPorRango(
                "2026/04/01/00:00:00",
                "2026/04/01/23:59:59",
                bookingList
        );

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(b -> b.getStartDate().startsWith("2026/04/01")));
    }

    @Test
    void testCalcularTotalIngresos() {
        double total = bookingServices.calcularTotalIngresos(bookingList);

        assertEquals(550.0, total, 0.001);
    }
}

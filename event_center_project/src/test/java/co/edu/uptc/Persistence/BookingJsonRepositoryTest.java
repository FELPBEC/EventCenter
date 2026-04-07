package co.edu.uptc.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

public class BookingJsonRepositoryTest {
    private static final String TEST_PATH = "test_booking.json";

    @AfterEach
    void limpiar() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_PATH));
    }

    @Test
    void testSaveBookingList() throws IOException {

    
        Path path = Paths.get(TEST_PATH);
        Files.writeString(path, "[]");

        BookingJsonRepository repo = new BookingJsonRepository(TEST_PATH);

        List<Booking> newBookings = new ArrayList<>();

        Client testClient = new Client("TestClient", 999, "pass", "555555555", "test@email.com", true);
        Salon testSalon = new Salon(99, "TestSalon", 50, 100.0);

        Booking testBooking = new Booking(
                999,
                testClient,
                testSalon,
                "2026/04/06/10:00:00",
                2,
                "2026/04/06/12:00:00"
        );
        testBooking.setPrice(200.0);

        newBookings.add(testBooking);

    
        repo.saveBookingList(newBookings);

        
        List<Booking> loadedBookings = repo.sendJsonBookingList();

        assertEquals(1, loadedBookings.size());

        Booking booking = loadedBookings.get(0);
        assertEquals(999, booking.getId());
        assertEquals("TestClient", booking.getClient().getUserName());
        assertEquals("TestSalon", booking.getSalon().getSalonName());
        assertEquals(200.0, booking.getPrice());
    }

    
    
}

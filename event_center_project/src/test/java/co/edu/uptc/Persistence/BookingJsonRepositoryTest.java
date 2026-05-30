package co.edu.uptc.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

public class BookingJsonRepositoryTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;

    @AfterEach
    void restoreUserDir() {
        if (originalUserDir != null) {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void testSaveBookingList() throws IOException {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        BookingJsonRepository repo = new BookingJsonRepository("test_booking.json");

        List<Booking> newBookings = new ArrayList<>();

        Client testClient = new Client("TestClient", 999, "pass", "555555555", "test@email.com", true);
        Salon testSalon = new Salon(99, "TestSalon", 50, 100.0);

        Booking testBooking = new Booking(
                999, testClient, testSalon, "2026/04/06/10:00:00", 2, "2026/04/06/12:00:00"
        );
        testBooking.setPrice(200.0);
        newBookings.add(testBooking);

        repo.saveBookingList(newBookings);
        List<Booking> loadedBookings = repo.sendJsonBookingList();

        assertNotNull(loadedBookings);
        assertEquals(1, loadedBookings.size());
        assertEquals(200.0, loadedBookings.get(0).getPrice());
    }
}
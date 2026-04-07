package co.edu.uptc.Persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.uptc.Model.Salon;

public class SalonJsonRepositoryTest {

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
    void testSaveSalonList() throws IOException {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        SalonJsonRepository repository = new SalonJsonRepository("salon_test.json");
        List<Salon> salons = new ArrayList<>();
        salons.add(new Salon(1, "Afrodita", 200, 230000.0));
        salons.add(new Salon(2, "Zeus", 12, 120000.0));

        repository.saveSalonList(salons);

        Path expectedFile = tempDir.resolve("data").resolve("salon_test.json");
        assertTrue(Files.exists(expectedFile), "El archivo debe crearse después de guardar");

        List<Salon> loadedSalons = repository.sendSalonList();

        assertNotNull(loadedSalons);
        assertEquals(2, loadedSalons.size());

        Salon first = loadedSalons.get(0);
        assertNotNull(first);
        assertEquals("Afrodita", first.getSalonName());
        assertEquals(200, first.getCapacity());
        assertEquals(230000.0, first.getPriceByHour());

        Salon second = loadedSalons.get(1);
        assertNotNull(second);
        assertEquals("Zeus", second.getSalonName());
        assertEquals(12, second.getCapacity());
    }

    @Test
    void testSendSalonList() throws IOException {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir);
        Path filePath = dataDir.resolve("salon_test_read.json");

        String json = "[\n" +
                "  {\n" +
                "    \"id\": 10,\n" +
                "    \"salonName\": \"Titan\",\n" +
                "    \"capacity\": 300,\n" +
                "    \"priceByHour\": 500000.0,\n" +
                "    \"numberOfReservations\": 5\n" +
                "  }\n" +
                "]";
        Files.writeString(filePath, json);

        SalonJsonRepository repository = new SalonJsonRepository("salon_test_read.json");
        List<Salon> loadedSalons = repository.sendSalonList();

        assertNotNull(loadedSalons);
        assertEquals(1, loadedSalons.size());

        Salon loaded = loadedSalons.get(0);

        assertNotNull(loaded);
        assertEquals(10, loaded.getId());
        assertEquals("Titan", loaded.getSalonName());
        assertEquals(300, loaded.getCapacity());
        assertEquals(500000.0, loaded.getPriceByHour());
        assertEquals(5, loaded.getNumberOfReservations());
    }
}

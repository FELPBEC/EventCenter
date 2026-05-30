package co.edu.uptc.Persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.uptc.Model.Client;

public class ClientJsonRepositoryTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;

    @AfterEach
    void restoreUserDir() {
        if (originalUserDir != null) {
            // Restaura la ruta original después de la prueba para no afectar otras partes
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void testSaveClientList() throws IOException {
        // Engañamos al sistema para que guarde en la carpeta temporal
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        ClientJsonRepository repo = new ClientJsonRepository("test_Cliente.json");

        List<Client> newClients = new ArrayList<>();
        newClients.add(new Client("TestClient", 999, "testpass", "555555555", "test@email.com", true));

        repo.saveClientList(newClients);

        List<Client> loadedClients = repo.sendJsonClientList();

        assertNotNull(loadedClients);
        assertEquals(1, loadedClients.size());
        assertEquals("TestClient", loadedClients.get(0).getUserName());
    }
}
package co.edu.uptc.Persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Client;

public class ClientJsonRepositoryTest {

    private static final String CLIENT_JSON_PATH = "test_Cliente.json"; // Ruta relativa desde test a raíz

    @Test
void testSaveClientList() throws IOException {

    Path path = Paths.get(CLIENT_JSON_PATH);

    
    if (!Files.exists(path)) {
        Files.createFile(path);
    }

    String originalContent = Files.readString(path);

    try {
        ClientJsonRepository repo = new ClientJsonRepository(CLIENT_JSON_PATH);

        List<Client> newClients = new ArrayList<>();
        newClients.add(new Client("TestClient", 999, "testpass", "555555555", "test@email.com", true));

        repo.saveClientList(newClients);

        List<Client> loadedClients = repo.sendJsonClientList();

        assertNotNull(loadedClients);
        assertEquals(1, loadedClients.size());

        Client client = loadedClients.get(0);

        assertNotNull(client);
        assertEquals("TestClient", client.getUserName());
        assertEquals(999, client.getId());
        assertTrue(client.isEmpresarial());

    } finally {
        Files.writeString(path, originalContent);
    }
}

  
}

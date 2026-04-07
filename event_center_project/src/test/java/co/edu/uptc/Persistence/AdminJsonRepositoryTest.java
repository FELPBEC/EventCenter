package co.edu.uptc.Persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Admin;


public class AdminJsonRepositoryTest {

    private static final String TEST_PATH = "test_admins.json";

    @Test
    void testSaveJsonAdminList() throws IOException {

        
        Path path = Paths.get(TEST_PATH);
        if (!Files.exists(path)) {
            Files.writeString(path, "[]");
        }

        AdminJsonRepository repo = new AdminJsonRepository(TEST_PATH);

        List<Admin> newAdmins = new ArrayList<>();
        newAdmins.add(new Admin("TestAdmin", 999, "testpass", "555555555", "test@email.com"));

        
        repo.saveJsonAdminList(newAdmins);

        
        List<Admin> loadedAdmins = repo.sendJsonAdminList();

        assertEquals(1, loadedAdmins.size());
        assertEquals("TestAdmin", loadedAdmins.get(0).getUserName());
    }

    @Test
    void testSendJsonAdminList() throws IOException {

        Path path = Paths.get(TEST_PATH);

        // Crear datos controlados
        String json = """
        [
            {
                "userName": "pepe",
                "id": 1234,
                "password": "1234",
                "phoneNumber": "3142159789",
                "email": "julian.moreno21@gmail.com"
            }
        ]
        """;

        Files.writeString(path, json);

        AdminJsonRepository repo = new AdminJsonRepository(TEST_PATH);

        List<Admin> admins = repo.sendJsonAdminList();

        assertNotNull(admins);
        assertEquals(1, admins.size());

        Admin admin = admins.get(0);
        assertEquals("pepe", admin.getUserName());
        assertEquals(1234, admin.getId());
        assertEquals("1234", admin.getPassword());
        assertEquals("3142159789", admin.getPhoneNumber());
        assertEquals("julian.moreno21@gmail.com", admin.getEmail());
    }

}

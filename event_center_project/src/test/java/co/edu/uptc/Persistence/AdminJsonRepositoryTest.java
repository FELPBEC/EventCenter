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

import co.edu.uptc.Model.Admin;

public class AdminJsonRepositoryTest {

    private static final String TEST_PATH = "test_admins.json";

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
    void testSaveJsonAdminList() throws IOException {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir);
        Path testFile = dataDir.resolve(TEST_PATH);

        if (!Files.exists(testFile)) {
            Files.writeString(testFile, "[]");
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
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir);
        Path testFile = dataDir.resolve(TEST_PATH);

        String json = "[\n" +
                "  {\n" +
                "    \"userName\": \"pepe\",\n" +
                "    \"id\": 1234,\n" +
                "    \"password\": \"1234\",\n" +
                "    \"phoneNumber\": \"3142159789\",\n" +
                "    \"email\": \"julian.moreno21@gmail.com\"\n" +
                "  }\n" +
                "]\n";

        Files.writeString(testFile, json);

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
